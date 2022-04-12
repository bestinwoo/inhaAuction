package project.inhaAuction.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.auth.dto.LoginDto;
import project.inhaAuction.auth.dto.RegisterDto;
import project.inhaAuction.auth.dto.TokenDto;
import project.inhaAuction.auth.dto.TokenRequestDto;
import project.inhaAuction.auth.repository.MemberRepository;
import project.inhaAuction.common.BasicResponse;
import project.inhaAuction.common.Result;
import project.inhaAuction.jwt.TokenProvider;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional(rollbackFor = Exception.class)
    public boolean join(RegisterDto authRequest) throws IOException {
        Member member = authRequest.toMember(passwordEncoder);
        if(validateDuplicateMember(member)) {
            return false;
        }

        memberRepository.save(member);

        MultipartFile image = authRequest.getImage();
        image.transferTo(new File("auth", "member-" + member.getId().toString() + ".png"));

        return true;
    }

    @Transactional(readOnly = true)
    public boolean validateDuplicateMember(Member member) {
        try {
            memberRepository.findByEmail(member.getEmail()).ifPresent(m -> {
                throw new IllegalStateException("이미 가입된 이메일입니다.");
            });

            memberRepository.findByloginId(member.getLoginId()).ifPresent(m -> {
                throw new IllegalStateException("이미 가입된 아이디입니다.");
            });
        } catch(IllegalStateException e) {
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public TokenDto login(LoginDto authRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = authRequest.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        redisTemplate.opsForValue()
                .set("RefreshToken:" + authentication.getName(), tokenDto.getRefreshToken(),
                        tokenDto.getRefreshTokenExpiresIn(), TimeUnit.MILLISECONDS); //TODO: 이거 시간 뭔가 단단히 잘못들어가고 있음.

        return tokenDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> reissue(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            return ResponseEntity.badRequest().body("Refresh Token이 유효하지 않습니다.");
            //throw new IllegalStateException("Refresh Token이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        //Redis에서 Refresh Token 가져오기
        String refreshToken = (String) redisTemplate.opsForValue().get("RefreshToken:" + authentication.getName());
        if(!refreshToken.equals(tokenRequestDto.getRefreshToken())) {
            return ResponseEntity.badRequest().body("토큰의 유저 정보가 일치하지 않습니다.");
        }

        //RefreshToken refreshToken = refreshTokenMapper.findByEmail(authentication.getName())
       //         .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        redisTemplate.opsForValue()
                .set("RefreshToken:" + authentication.getName(), tokenDto.getRefreshToken(),
                        tokenDto.getRefreshTokenExpiresIn(), TimeUnit.MILLISECONDS);

        return ResponseEntity.ok(new Result<>(tokenDto));
    }


}

package project.inhaAuction.auth.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
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
import project.inhaAuction.auth.dto.*;
import project.inhaAuction.auth.repository.MemberRepository;
import project.inhaAuction.common.Result;
import project.inhaAuction.jwt.SecurityUtil;
import project.inhaAuction.jwt.TokenProvider;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
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
        if(!image.getContentType().contains("png")) {
            memberRepository.delete(member);
            throw new FileUploadException("파일 확장자는 png 파일만 업로드 가능합니다.");
        }
        image.transferTo(new File("auth", "member-" + member.getId().toString() + ".png"));

        return true;
    }

    @Transactional(readOnly = true)
    public boolean validateDuplicateMember(Member member) {
        try {
            memberRepository.findByEmail(member.getEmail()).ifPresent(m -> {
                throw new IllegalStateException("이미 가입된 이메일입니다.");
            });

            memberRepository.findByLoginId(member.getLoginId()).ifPresent(m -> {
                throw new IllegalStateException("이미 가입된 아이디입니다.");
            });
        } catch(IllegalStateException e) {
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public MemberDto.Response getMemberInfo(String loginId) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        return member.map(MemberDto.Response::of).get();
    }

    @Transactional(rollbackFor = Exception.class)
    public TokenDto login(LoginDto authRequest) throws IllegalStateException {
        UsernamePasswordAuthenticationToken authenticationToken = authRequest.toAuthentication();
        //왜 authRequest.toAuthentication은 loginId로 토큰을 만드는데 Long id값으로 authentication이 만들어 지는 것일까? 예상 : customUserDetailsService의 loadUsername에서 loginId로 유저를 찾아서 그런듯?
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        MemberDto.Response memberInfo = getMemberInfo(authRequest.getId());
        if(memberInfo.getBanDate() != null) {
            if(memberInfo.getBanDate().getTime() > new Date().getTime()) {
                throw new IllegalStateException("정지된 회원입니다. " + memberInfo.getBanDate() + "일까지 이용할 수 없습니다.");
            }
        }
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        tokenDto.setMemberId(memberInfo.getId());
        tokenDto.setState(memberInfo.getState());
        redisTemplate.opsForValue()
                .set("RefreshToken:" + authentication.getName(), tokenDto.getRefreshToken(),
                        tokenDto.getRefreshTokenExpiresIn() - new Date().getTime(), TimeUnit.MILLISECONDS);

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
                        tokenDto.getRefreshTokenExpiresIn() - new Date().getTime(), TimeUnit.MILLISECONDS);

        return ResponseEntity.ok(new Result<>(tokenDto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void changePassword(MemberDto.changePassword memberDto) throws IllegalStateException, SecurityException {
        if(!SecurityUtil.getCurrentMemberId().equals(memberDto.getId())) {
            throw new SecurityException("본인 계정의 정보만 수정할 수 있습니다.");
        }
        Optional<Member> member = memberRepository.findById(memberDto.getId());
        member.ifPresentOrElse(m -> {
            if(passwordEncoder.matches(memberDto.getCurrentPassword(), m.getPassword())) {
                m.changePassword(passwordEncoder.encode(memberDto.getNewPassword()));
            } else {
                throw new IllegalStateException("현재 비밀번호가 일치하지 않습니다.");
            }
        }, () -> {
            throw new IllegalStateException("해당 유저가 존재하지 않습니다.");
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void modifyMemberInfo(Long id, MemberDto.modifyInfo memberDto) throws IllegalStateException, SecurityException {
        if(!SecurityUtil.getCurrentMemberId().equals(id)) {
            throw new SecurityException("본인 계정의 정보만 수정할 수 있습니다.");
        }

        Optional<Member> member = memberRepository.findById(id);
        member.ifPresentOrElse(m -> {
            m.modifyInfo(memberDto.getEmail(), memberDto.getAddress(), memberDto.getPhone());
        }, () -> {
            throw new IllegalStateException("해당 유저가 존재하지 않습니다.");
        });
    }


}

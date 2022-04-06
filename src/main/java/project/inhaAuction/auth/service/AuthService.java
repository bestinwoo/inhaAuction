package project.inhaAuction.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.auth.dto.LoginDto;
import project.inhaAuction.auth.dto.RegisterDto;
import project.inhaAuction.auth.dto.TokenDto;
import project.inhaAuction.auth.repository.MemberRepository;
import project.inhaAuction.jwt.TokenProvider;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public boolean join(RegisterDto authRequest) {
        Member member = authRequest.toMember(passwordEncoder);
        if(validateDuplicateMember(member)) {
            return false;
        }

        memberRepository.save(member);
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

    @Transactional
    public TokenDto login(LoginDto authRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = authRequest.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        redisTemplate.opsForValue()
                .set("RefreshToken:" + authentication.getName(), tokenDto.getRefreshToken(),
                        tokenDto.getRefreshTokenExpiresIn(), TimeUnit.MILLISECONDS);
       /* RefreshToken refreshToken = RefreshToken.builder()
                .email(authentication.getName())
                .token(tokenDto.getRefreshToken())
                .build();

        Optional<RefreshToken> oldRefreshToken = refreshTokenMapper.findByEmail(authRequest.getEmail());

        oldRefreshToken.ifPresentOrElse(m -> refreshTokenMapper.update(refreshToken), () -> refreshTokenMapper.save(refreshToken));
*/
        return tokenDto;
    }


}

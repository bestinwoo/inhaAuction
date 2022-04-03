package project.inhaAuction.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.auth.dto.AuthRequest;
import project.inhaAuction.auth.repository.MemberRepository;
import project.inhaAuction.jwt.TokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public boolean join(AuthRequest authRequest) {
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
}

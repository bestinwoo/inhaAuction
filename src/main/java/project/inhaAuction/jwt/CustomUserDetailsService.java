package project.inhaAuction.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.auth.repository.MemberRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByLoginId(username).map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + "는 데이터베이스에 없는 데이터입니다."));
    }

    private UserDetails createUserDetails(Member member) {
        String role = "ROLE_USER";
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(member.getLoginId()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}

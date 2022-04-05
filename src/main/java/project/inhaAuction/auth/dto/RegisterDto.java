package project.inhaAuction.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.inhaAuction.auth.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String id; // login_id
    private String name;
    private String email;
    private String department;
    private String password;
    private String address;
    private String phone;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .loginId(id)
                .name(name)
                .email(email)
                .department(department)
                .password(passwordEncoder.encode(password))
                .address(address)
                .phone(phone)
                .build();
    }

}

package project.inhaAuction.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import project.inhaAuction.auth.domain.Member;

@Getter
@Setter
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
    private MultipartFile image;

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

package project.inhaAuction.auth.dto;

import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import project.inhaAuction.auth.domain.Member;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    @NonNull
    private String id; // login_id
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String department;
    @NonNull
    private String password;
    @NonNull
    private String address;
    @NonNull
    private String phone;
    @NonNull
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

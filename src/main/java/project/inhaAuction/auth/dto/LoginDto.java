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
public class LoginDto {
    private String id; // login_id
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(id, password);
    }
}

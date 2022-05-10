package project.inhaAuction.auth.dto;

import lombok.Builder;
import lombok.Data;
import project.inhaAuction.auth.domain.ROLE;

@Data
@Builder
public class TokenDto {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;
    private Long refreshTokenExpiresIn;
    private String refreshToken;
    private Long memberId;
    private ROLE state;
}


package project.inhaAuction.auth.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.inhaAuction.auth.dto.LoginDto;
import project.inhaAuction.auth.dto.RegisterDto;
import project.inhaAuction.auth.dto.TokenDto;
import project.inhaAuction.auth.service.AuthService;
import project.inhaAuction.common.BasicResponse;
import project.inhaAuction.common.ErrorResponse;
import project.inhaAuction.common.Result;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BasicResponse> register(@RequestBody RegisterDto authRequest) {
        if(authService.join(authRequest)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new Result<>("회원가입이 완료되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("이미 가입된 회원입니다.", "403"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

}

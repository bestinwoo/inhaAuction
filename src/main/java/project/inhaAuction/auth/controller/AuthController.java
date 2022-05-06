package project.inhaAuction.auth.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.inhaAuction.auth.dto.*;
import project.inhaAuction.auth.service.AuthService;
import project.inhaAuction.common.BasicResponse;
import project.inhaAuction.common.ErrorResponse;
import project.inhaAuction.common.Result;

import java.io.IOException;

@CrossOrigin()
@RestController

@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/register")
    public ResponseEntity<BasicResponse> register(@ModelAttribute RegisterDto authRequest) throws IOException {
        if(authRequest.getImage() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("학생 인증 파일이 필요합니다.", "403"));
        }
        if(authService.join(authRequest)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new Result<>("회원가입이 완료되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("이미 가입된 회원입니다.", "403"));
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return authService.reissue(tokenRequestDto);
    }

    @GetMapping("/auth/{id}")
    public ResponseEntity<BasicResponse> getMemberInfo(@PathVariable String id) {
        MemberDto.Response memberInfo = authService.getMemberInfo(id);
        if(memberInfo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("회원 정보를 찾을 수 없습니다."));
        } else {
            return ResponseEntity.ok(new Result<>(memberInfo));
        }
    }

    @PostMapping("/password")
    public ResponseEntity<BasicResponse> changePassword(@RequestBody MemberDto.changePassword memberDto) {
        try {
            authService.changePassword(memberDto);
            return ResponseEntity.ok(new Result<>("비밀번호 변경 완료"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "400"));
        }
    }

   /* @PostMapping("/{id}")
    public void modifyMemberInfo(@PathVariable Long id) {

    }*/

}

package project.inhaAuction.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.inhaAuction.auth.dto.MemberDto;
import project.inhaAuction.common.BasicResponse;
import project.inhaAuction.common.ErrorResponse;
import project.inhaAuction.common.Result;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/members")
    public ResponseEntity<BasicResponse> getMembers(String keyword, int page, int per_page) {
        List<MemberDto.Response> members = adminService.getMembers(keyword, page, per_page);
        if(members.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("회원이 없습니다."));
        }

        return ResponseEntity.ok(new Result<>(members, adminService.getMembersCount(keyword)));
    }
}

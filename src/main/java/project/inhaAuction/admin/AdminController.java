package project.inhaAuction.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.inhaAuction.auth.domain.Role;
import project.inhaAuction.auth.dto.MemberDto;
import project.inhaAuction.common.BasicResponse;
import project.inhaAuction.common.ErrorResponse;
import project.inhaAuction.common.Result;
import project.inhaAuction.report.ReportService;
import project.inhaAuction.report.dto.ReportDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final ReportService reportService;

    @GetMapping("/members")
    public ResponseEntity<BasicResponse> getMembers(String keyword, int page, int per_page) {
        List<MemberDto.Response> members = adminService.getMembers(keyword, page, per_page);
        if(members.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("회원이 없습니다."));
        }

        return ResponseEntity.ok(new Result<>(members, adminService.getMembersCount(keyword)));
    }

    @PostMapping("/members")
    public ResponseEntity<BasicResponse> modifyMemberState(@RequestBody Map<Long, Role> map) {
        try {
            adminService.modifyMemberState(map);
            return ResponseEntity.ok(new Result<>("회원 상태 변경이 완료되었습니다."));
        } catch(IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "400"));
        }
    }

    @PostMapping("/members/ban")
    public ResponseEntity<BasicResponse> memberBan(@RequestBody Map<Long, Date> map) {
        try {
            adminService.memberBan(map);
            return ResponseEntity.ok(new Result<>("회원 정지가 완료되었습니다."));
        } catch(IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "400"));
        }
    }

    //TODO: 탈퇴시 이미지파일 삭제하기
    @DeleteMapping("/members/{id}")
    public ResponseEntity<BasicResponse> deleteMember(@PathVariable Long id) {
        try {
            adminService.deleteMember(id);
            return ResponseEntity.ok(new Result<>("삭제 완료"));
        } catch(IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "400"));
        }
    }

    @GetMapping("/reports")
    public ResponseEntity<BasicResponse> getReports(@RequestParam int page, @RequestParam int per_page) {
        List<ReportDto.Response> reports = reportService.getReports(page, per_page);
        if(reports.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("신고 정보가 없습니다."));
        }
        return ResponseEntity.ok(new Result<>(reports, reportService.getReportsCount()));
    }
}



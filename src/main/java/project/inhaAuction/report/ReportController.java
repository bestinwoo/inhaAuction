package project.inhaAuction.report;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.inhaAuction.common.BasicResponse;
import project.inhaAuction.common.ErrorResponse;
import project.inhaAuction.common.Result;
import project.inhaAuction.report.dto.ReportDto;

import java.io.IOException;

@Controller
@CrossOrigin()
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<BasicResponse> writeReport(@ModelAttribute ReportDto.Write write) {
        try {
            reportService.writeReport(write);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Result<>("신고 작성 완료"));
        } catch(IOException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "400"));
        }
    }

}

package project.inhaAuction.report;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.inhaAuction.report.domain.Report;
import project.inhaAuction.report.dto.ReportDto;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    @Transactional(rollbackFor = Exception.class)
    public void writeReport(ReportDto.Write write) throws IOException {
        List<MultipartFile> files = write.getFiles();
        if(!files.isEmpty()) {
            write.setImgCnt(files.size() + 0L);
        }
        Report report = write.toReport();
        reportRepository.save(report);
        for (MultipartFile file : files) {
            if(!file.getContentType().contains("png")) {
                throw new FileUploadException("파일 확장자는 png 파일만 업로드 가능합니다.");
            }
            file.transferTo(new File("report", "report-" + report.getId().toString() + "-" + files.indexOf(file) + ".png"));
        }
    }

    @Transactional(readOnly = true)
    public List<ReportDto.Response> getReports(int page, int per_page) {
        List<Report> reports = reportRepository.findByPage(page, per_page);
        return reports.stream().map(ReportDto.Response::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Integer getReportsCount() {
        return reportRepository.getReportCount();
    }
}

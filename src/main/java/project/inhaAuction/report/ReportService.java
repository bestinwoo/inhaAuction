package project.inhaAuction.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.inhaAuction.report.domain.Report;
import project.inhaAuction.report.dto.ReportDto;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
            file.transferTo(new File("report", "report-" + report.getId().toString() + "-" + files.indexOf(file) + ".png"));
        }
    }
}

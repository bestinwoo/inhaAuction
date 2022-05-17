package project.inhaAuction.report.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.product.domain.Product;
import project.inhaAuction.report.domain.Report;

import java.util.ArrayList;
import java.util.List;

public class ReportDto {
    @Getter
    @Setter
    public static class Write {
        private Long productId;
        private String title;
        private Long writerId;
        private Long reportedId;
        private String content;
        private Long imgCnt = 0L;
        private List<MultipartFile> files = new ArrayList<>();

        public Report toReport() {
            return Report.builder()
                    .writer(Member.builder().id(writerId).build())
                    .product(Product.builder().id(productId).build())
                    .state("N")
                    .reported(Member.builder().id(reportedId).build())
                    .content(this.content)
                    .title(this.title)
                    .imgCnt(this.imgCnt)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private Long productId;
        private String productName;
        private String title;
        private String writerLoginId;
        private String reportedLoginId;
        private String content;
        private Long imgCnt;

        public static Response of(Report report) {
            return Response.builder()
                    .id(report.getId())
                    .productId(report.getProduct().getId())
                    .productName(report.getProduct().getName())
                    .title(report.getTitle())
                    .writerLoginId(report.getWriter() == null ? null : report.getWriter().getLoginId())
                    .reportedLoginId(report.getReported() == null ? null : report.getReported().getLoginId())
                    .content(report.getContent())
                    .imgCnt(report.getImgCnt())
                    .build();
        }
    }
}

package project.inhaAuction.report.dto;

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
        private String content;
        private Long imgCnt = 0L;
        private List<MultipartFile> files = new ArrayList<>();

        public Report toReport() {
            return Report.builder()
                    .writer(Member.builder().id(writerId).build())
                    .product(Product.builder().id(productId).build())
                    .state("N")
                    .content(this.content)
                    .title(this.title)
                    .imgCnt(this.imgCnt)
                    .build();
        }
    }

    @Getter
    public static class Response {
        private Long productId;
        private String title;
        private Long writerId;
        private Long reportedId;
        private String content;
        private List<MultipartFile> files;
    }
}

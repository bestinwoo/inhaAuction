package project.inhaAuction.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.product.domain.Category;
import project.inhaAuction.product.domain.Product;

import java.time.LocalDateTime;

public class ProductDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Detail {
        private Long id;
        private String name;
        private Long startPrice;
        private Long categoryId;
        private String content;
        private Long instantPrice;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Long bidUnit;
        private String sellerId;
        private Long bidderCnt;
        private Long imgCnt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Summary {
        private Long id;
        private String name;
        private Long startPrice;
        private Long instantPrice;
        private LocalDateTime endDate;
        private String sellerId;
        private Long bidderCnt;
        private Long imgCnt;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        private String name;
        private Long startPrice;
        private Long categoryId;
        private String content;
        private Long instantPrice;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime endDate;
        private Long bidUnit;
        private Long sellerId;
        private Long imgCnt;

        public Product toProduct() {
            return Product.builder()
                    .name(name)
                    .startPrice(startPrice)
                    .category(Category.builder()
                            .id(categoryId)
                            .build())
                    .content(content)
                    .instantPrice(instantPrice)
                    .endDate(endDate)
                    .bidUnit(bidUnit)
                    .seller(Member.builder()
                            .id(sellerId)
                            .build())
                    .imgCnt(imgCnt)
                    .build();
        }
    }


}

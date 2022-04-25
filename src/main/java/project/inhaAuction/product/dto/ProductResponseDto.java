package project.inhaAuction.product.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private Long startPrice;
    private Long categoryId;
    private String content;
    private Long instantPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long bidUnit;
    private Long sellerId;
    private Long bidderCnt;

  /*  public Product toProduct() {
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
                .bidderCnt(bidderCnt)
                .currentPrice(currentPrice)
                .build();
    }*/
}

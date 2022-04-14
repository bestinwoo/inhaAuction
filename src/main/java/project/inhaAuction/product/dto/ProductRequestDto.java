package project.inhaAuction.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.product.domain.Category;
import project.inhaAuction.product.domain.Product;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    private String name;
    private Long startPrice;
    private Long categoryId;
    private String content;
    private Long instantPrice;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;
    private Long bidUnit;
    private Long sellerId;

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
                .build();
    }
}

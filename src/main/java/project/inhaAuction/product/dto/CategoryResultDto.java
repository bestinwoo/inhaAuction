package project.inhaAuction.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.inhaAuction.product.domain.Category;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResultDto {
    private Long id;
    private String name;
    private Long depth;
    private List<CategoryResultDto> children;

    public static CategoryResultDto of(Category category) {
        return new CategoryResultDto(
                category.getId(),
                category.getName(),
                category.getDepth(),
                category.getChildren().stream().map(CategoryResultDto::of).collect(Collectors.toList())
        );
    }

}

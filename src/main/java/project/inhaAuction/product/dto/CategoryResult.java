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
public class CategoryResult {
    private Long id;
    private String name;
    private Long depth;
    private List<CategoryResult> children;

    public static CategoryResult of(Category category) {
        return new CategoryResult(
                category.getId(),
                category.getName(),
                category.getDepth(),
                category.getChildren().stream().map(CategoryResult::of).collect(Collectors.toList())
        );
    }

}

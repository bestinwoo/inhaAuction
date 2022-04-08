package project.inhaAuction.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.inhaAuction.product.dto.CategoryResult;
import project.inhaAuction.product.repository.CategoryRepository;
import project.inhaAuction.product.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(rollbackFor = Exception.class)
    public List<CategoryResult> getCategoryList() {
        List<CategoryResult> results = categoryRepository.findAll().stream().map(CategoryResult::of).collect(Collectors.toList());
        return results;
    }
}

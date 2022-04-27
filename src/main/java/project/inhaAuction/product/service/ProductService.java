package project.inhaAuction.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.inhaAuction.product.domain.Product;
import project.inhaAuction.product.dto.CategoryResultDto;
import project.inhaAuction.product.dto.ProductDto;
import project.inhaAuction.product.repository.CategoryRepository;
import project.inhaAuction.product.repository.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(rollbackFor = Exception.class)
    public List<CategoryResultDto> getCategoryList() {
        List<CategoryResultDto> results = categoryRepository.findAll().stream().map(CategoryResultDto::of).collect(Collectors.toList());
        return results;
    }

    @Transactional(rollbackFor = Exception.class) //TODO: 확장자 체크
    public ProductDto.Detail addProduct(ProductDto.Request productDto, List<MultipartFile> multipartFiles) throws IOException {
        productDto.setImgCnt(multipartFiles.size() + 0L);
        Product product = productDto.toProduct();
        productRepository.save(product);
        for (MultipartFile multipartFile : multipartFiles) {
            multipartFile.transferTo(new File("product", "product-" + product.getId().toString() + "-" + multipartFiles.indexOf(multipartFile) + ".png"));
        }

        return toProductDetail(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDto.Summary> getProductList(String keyword, String categoryName, int page, int per_page, String sort) {
        List<Product> products;

        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }
        products = productRepository.findByCategoryAndKeyword(keyword, categoryName, page, per_page, sort);

        List<ProductDto.Summary> result = products.stream().map(this::toProductSummary).collect(Collectors.toList());

        return result;
    }

    @Transactional(readOnly = true)
    public Integer getProductCount(String keyword, String categoryName, int page, int per_page) {
        return productRepository.getProductCount(keyword, categoryName, page, per_page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ProductDto.Detail getProductDetail(Long id) throws IllegalStateException {
        Product product = productRepository.getProductDetail(id).orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다."));

        return toProductDetail(product);
    }

    private ProductDto.Summary toProductSummary(final Product product) {
        return ProductDto.Summary.builder()
                .id(product.getId())
                .instantPrice(product.getInstantPrice())
                .endDate(product.getEndDate())
                .name(product.getName())
                .bidderCnt(product.getBidderCnt())
                .startPrice(product.getStartPrice())
                .sellerId(product.getSeller().getLoginId())
                .imgCnt(product.getImgCnt())
                .build();
    }

    private ProductDto.Detail toProductDetail(final Product product) {
        return ProductDto.Detail.builder()
                .id(product.getId())
                .name(product.getName())
                .startPrice(product.getStartPrice())
                .categoryId(product.getCategory().getId())
                .content(product.getContent())
                .instantPrice(product.getInstantPrice())
                .endDate(product.getEndDate())
                .bidUnit(product.getBidUnit())
                .startDate(product.getStartDate())
                .sellerId(product.getSeller().getLoginId())
                .bidderCnt(product.getBidderCnt())
                .imgCnt(product.getImgCnt())
                .build();
    }
}

package project.inhaAuction.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.inhaAuction.product.domain.Product;
import project.inhaAuction.product.dto.CategoryResultDto;
import project.inhaAuction.product.dto.ProductResponseDto;
import project.inhaAuction.product.dto.ProductRequestDto;
import project.inhaAuction.product.repository.CategoryRepository;
import project.inhaAuction.product.repository.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
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

    @Transactional(rollbackFor = Exception.class)
    public ProductResponseDto addProduct(ProductRequestDto productDto, List<MultipartFile> multipartFiles) throws IOException {
        Product product = productDto.toProduct();
        productRepository.save(product);
        for (MultipartFile multipartFile : multipartFiles) {
            multipartFile.transferTo(new File("product", "product-" + product.getId().toString() + "-" + multipartFiles.indexOf(multipartFile) + ".png"));
        }


        return toProductDto(product);
    }

    private ProductResponseDto toProductDto(final Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .startPrice(product.getStartPrice())
                .categoryId(product.getCategory().getId())
                .content(product.getContent())
                .instantPrice(product.getInstantPrice())
                .endDate(product.getEndDate())
                .bidUnit(product.getBidUnit())
                .sellerId(product.getSeller().getId())
                .bidderCnt(product.getBidderCnt())
                .currentPrice(product.getCurrentPrice())
                .build();
    }
}

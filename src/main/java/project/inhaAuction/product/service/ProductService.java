package project.inhaAuction.product.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.inhaAuction.product.domain.Product;
import project.inhaAuction.product.dto.CategoryResultDto;
import project.inhaAuction.product.dto.ProductDto;
import project.inhaAuction.product.repository.CategoryRepository;
import project.inhaAuction.product.repository.ProductRepository;

import javax.persistence.EntityNotFoundException;
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
    public ProductDto.Detail addProduct(ProductDto.Request productDto, List<MultipartFile> multipartFiles) throws IOException, IllegalStateException {
        if(multipartFiles == null) {
            throw new IllegalStateException("상품 이미지가 없습니다.");
        }

        productDto.setImgCnt((long) multipartFiles.size());
        Product product = productDto.toProduct();
        productRepository.save(product);
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.getContentType().contains("png")) {
                productRepository.deleteById(product.getId());
                throw new FileUploadException("파일 확장자는 png 파일만 업로드 가능합니다.");
            }
            multipartFile.transferTo(new File("product", "product-" + product.getId().toString() + "-" + multipartFiles.indexOf(multipartFile) + ".png"));
        }

        return toProductDetail(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDto.Summary> getProductList(String keyword, String categoryName, int page, int per_page) {
        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }
        List<Product> products = productRepository.findByCategoryAndKeyword(keyword, categoryName, page, per_page);

        List<ProductDto.Summary> result = products.stream().map(this::toProductSummary).collect(Collectors.toList());

        return result;
    }

    @Transactional(readOnly = true)
    public Integer getProductCount(String keyword, String categoryName) {
        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }
        return productRepository.getProductCount(keyword, categoryName);
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
        String sellerLoginId = null;

        if(product.getSeller() != null) {
            sellerLoginId = product.getSeller().getLoginId();
        }
        return ProductDto.Summary.builder()
                .id(product.getId())
                .instantPrice(product.getInstantPrice())
                .endDate(product.getEndDate())
                .name(product.getName())
                .bidderCnt(product.getBidderCnt())
                .startPrice(product.getStartPrice())
                .sellerLoginId(sellerLoginId)
                .successBidderId(getSuccessBidderId(product))
                .successBid(product.getSuccessBid())
                .imgCnt(product.getImgCnt())
                .build();
    }

    private ProductDto.Detail toProductDetail(final Product product) {
        String sellerLoginId = null;
        Long sellerId = null;
        if(product.getSeller() != null) {
            sellerId = product.getSeller().getId();
            sellerLoginId = product.getSeller().getLoginId();
        }

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
                .sellerLoginId(sellerLoginId)
                .sellerId(sellerId)
                .bidderCnt(product.getBidderCnt())
                .imgCnt(product.getImgCnt())
                .successBidderId(getSuccessBidderId(product))
                .successBid(product.getSuccessBid())
                .build();
    }

    private String getSuccessBidderId(Product product) {
        String successBidderLoginId = null;
        try {
            if(product.getSuccessBidder() != null)
                successBidderLoginId = product.getSuccessBidder().getLoginId();
        } catch(EntityNotFoundException e) {
            System.out.println("e = " + e);;
        }

        return successBidderLoginId;
    }
}

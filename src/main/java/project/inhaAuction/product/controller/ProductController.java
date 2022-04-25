package project.inhaAuction.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.inhaAuction.common.BasicResponse;
import project.inhaAuction.common.ErrorResponse;
import project.inhaAuction.common.Result;
import project.inhaAuction.product.dto.ProductRequestDto;
import project.inhaAuction.product.service.ProductService;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/categories") //TODO: 카테고리 이름으로 검색 추가?
    public ResponseEntity<?> getCategoryList() {
        return ResponseEntity.ok(productService.getCategoryList());
    }

    @PostMapping
    public ResponseEntity<BasicResponse> addProduct(@ModelAttribute ProductRequestDto product, @ModelAttribute List<MultipartFile> files) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new Result<>(productService.addProduct(product, files)));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("상품 사진 업로드 실패", "403"));
        }
    }

    @GetMapping()
    public ResponseEntity<BasicResponse> getProducts(@RequestParam(required = false) String keyword, @RequestParam(required = false) String categoryName,
                                                     @RequestParam int page, @RequestParam int per_page, @RequestParam String sort) {
        return ResponseEntity.ok(new Result<>(productService.getProductList(keyword, categoryName, page, per_page, sort),
                productService.getProductCount(keyword, categoryName, page, per_page)));
    }

}

package project.inhaAuction.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.inhaAuction.common.BasicResponse;
import project.inhaAuction.common.ErrorResponse;
import project.inhaAuction.common.Result;
import project.inhaAuction.product.dto.ProductDto;
import project.inhaAuction.product.service.ProductService;

import java.io.IOException;
import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    //모든 카테고리 조회
    @GetMapping("/categories") //TODO: 카테고리 이름으로 검색 추가?
    public ResponseEntity<?> getCategoryList() {
        return ResponseEntity.ok(productService.getCategoryList());
    }

    //상품 등록
    @PostMapping
    public ResponseEntity<BasicResponse> addProduct(@ModelAttribute ProductDto.Request product, @ModelAttribute List<MultipartFile> files) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new Result<>(productService.addProduct(product, files)));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("상품 사진 업로드 실패", "403"));
        }
    }

    //상품 목록 조회(검색어, 카테고리명) TODO: 상품 이미지 처리
    @GetMapping()
    public ResponseEntity<BasicResponse> getProducts(@RequestParam(required = false) String keyword, @RequestParam(required = false) String categoryName,
                                                     @RequestParam int page, @RequestParam int per_page, @RequestParam String sort) {
        return ResponseEntity.ok(new Result<>(productService.getProductList(keyword, categoryName, page, per_page, sort),
                productService.getProductCount(keyword, categoryName, page, per_page)));
    }

    //상품 삭제 TODO: 삭제 예외처리, 입찰자가 있는 상품일 경우 외래키 처리
    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new Result<>("삭제 완료"));
    }

    //상품 상세 TODO: 상품 이미지 처리
    @GetMapping("/{id}")
    public ResponseEntity<BasicResponse> getProductDetail(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new Result<>(productService.getProductDetail(id)));
        } catch(IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }
}
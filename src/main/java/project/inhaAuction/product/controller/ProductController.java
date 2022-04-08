package project.inhaAuction.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.inhaAuction.product.service.ProductService;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/categorys")
    public ResponseEntity<?> getCategoryList() {
        return ResponseEntity.ok(productService.getCategoryList());
    }
}

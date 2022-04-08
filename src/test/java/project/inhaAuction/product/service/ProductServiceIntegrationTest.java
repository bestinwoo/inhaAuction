package project.inhaAuction.product.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.inhaAuction.product.repository.CategoryRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceIntegrationTest {
    @Autowired CategoryRepository categoryRepository;

    @Test
    void getCategoryList() {
    }
}
package project.inhaAuction.product.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.inhaAuction.product.domain.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryRepositoryTest {
    @Autowired CategoryRepository categoryRepository;
    
    @Test
    @DisplayName("카테고리 리스트 출력 확인")
    void findAll() {
        List<Category> list = categoryRepository.findAll();
        for (Category category : list) {
            if(category.getParent() != null) {
                System.out.println("category = " + category.getName() + " parent Id= " + category.getParent().getName());

                for (Category child : category.getChildren()) {
                    System.out.println("child.getName() = " + child.getName());
                }
            }
        }
    }
}
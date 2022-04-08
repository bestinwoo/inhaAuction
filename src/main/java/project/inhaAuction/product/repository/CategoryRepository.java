package project.inhaAuction.product.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.inhaAuction.product.domain.Category;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {
    private final EntityManager em;

    public List<Category> findAll() {
        return em.createQuery("select c from Category c where c.parent is NULL", Category.class).getResultList();
    }
}

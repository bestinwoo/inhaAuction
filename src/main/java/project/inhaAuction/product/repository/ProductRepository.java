package project.inhaAuction.product.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.inhaAuction.product.domain.Product;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final EntityManager em;

    public Product save(Product product) {
        em.persist(product);
        return product;
    }

    public List<Product> findAll() {
        return em.createQuery("select p from Product p", Product.class).getResultList();
    }

    public List<Product> findByCategoryId(Long id) {
        return em.createQuery("select p from Product p where Product.category.id = :id", Product.class)
                .setParameter("id", id)
                .getResultList();

    }


}

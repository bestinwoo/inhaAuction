package project.inhaAuction.product.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.inhaAuction.product.domain.Product;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final EntityManager em;

    public Product save(Product product) {
        em.persist(product);
        return product;
    }

    //TODO: 정렬 구현
    public List<Product> findByCategoryAndKeyword(String keyword, String categoryName, int page, int per_page, String sort) {
        return em.createQuery("select p from Product p where (:keyword is null or p.name like :keyword)" +
                        " and (:categoryName is null or p.category.name like :categoryName)", Product.class)
                .setParameter("keyword", keyword)
                .setParameter("categoryName", categoryName)
                .setFirstResult(page)
                .setMaxResults(per_page)
                .getResultList();
    }

    public Integer getProductCount(String keyword, String categoryName, int page, int per_page) {
        return Integer.parseInt(em.createQuery("select count(p) from Product p where (:keyword is null or p.name like :keyword)" +
                " and (:categoryName is null or p.category.name like :categoryName)")
                .setParameter("keyword", keyword)
                .setParameter("categoryName", categoryName)
                .getSingleResult()
                .toString());
    }

    public Optional<Product> getProductDetail(Long id) {
        Product product = em.find(Product.class, id);
        return Optional.ofNullable(product);
    }

    public void deleteById(Long id) {
        Product product = em.find(Product.class, id);
        em.remove(product);
    }
}

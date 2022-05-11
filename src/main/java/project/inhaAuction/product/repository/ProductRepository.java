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
    //TODO: 그리고 상위 카테고리 선택하면 하위카테고리까지 포함되게 가능?
    public List<Product> findByCategoryAndKeyword(String keyword, String categoryName, int page, int per_page) {
        return em.createQuery("select p from Product p where (:keyword is null or p.name like :keyword)" +
                        " and (:categoryName is null or p.category.name like :categoryName) order by p.successBid, p.endDate desc", Product.class)
                .setParameter("keyword", keyword)
                .setParameter("categoryName", categoryName)
                .setFirstResult((page - 1) * per_page)
                .setMaxResults(per_page)
                .getResultList();
    }

    public Integer getProductCount(String keyword, String categoryName) {
        return Integer.parseInt(em.createQuery("select count(p) from Product p where (:keyword is null or p.name like :keyword)" +
                " and (:categoryName is null or p.category.name like :categoryName)")
                .setParameter("keyword", keyword)
                .setParameter("categoryName", categoryName)
                .getSingleResult()
                .toString());
    }

    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    public List<Product> findByMemberId(Long id) {
        return em.createQuery("select p from Product p where p.seller.id = :id", Product.class)
                .setParameter("id", id)
                .getResultList();
    }

    public Optional<Product> getProductDetail(Long id) {
        Product product = findById(id);
        return Optional.ofNullable(product);
    }

    public void increaseBidderCntById(Long id) {
        Product product = findById(id);
        product.increaseBidderCnt();
    }

    public void successBidByIdAndPrice(Long pId, Long cId, Long price) throws IllegalStateException{
        Product product = findById(pId);
        if(product.getSuccessBid() != null) {
            throw new IllegalStateException("이미 낙찰된 상품입니다.");
        }
        product.setSuccessBidderId(cId, price);
    }

    public void deleteById(Long id) {
        Product product = findById(id);
        em.remove(product);
    }
}

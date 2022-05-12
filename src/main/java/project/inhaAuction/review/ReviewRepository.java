package project.inhaAuction.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.inhaAuction.review.domain.Review;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {
    private final EntityManager em;

    public Review save(Review review) {
        em.persist(review);
        return review;
    }

    public List<Review> findByMemberId(Long id) {
        return em.createQuery("select r from Review r where r.seller.id = :id", Review.class)
                .setParameter("id", id)
                .getResultList();
    }

    public Integer countByProductId(Long id) {
        return Integer.parseInt(em.createQuery("select count(r) from Review r where r.product.id = :id")
                .setParameter("id", id)
                .getSingleResult()
                .toString());
    }
}

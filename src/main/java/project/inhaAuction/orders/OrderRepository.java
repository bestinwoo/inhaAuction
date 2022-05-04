package project.inhaAuction.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.inhaAuction.orders.domain.Orders;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public Orders save(Orders order) {
        em.persist(order);
        return order;
    }

    public Optional<Orders> findByProductIdAndCustomerId(Long productId, Long customerId) {
        List<Orders> orders = em.createQuery("select o from Orders o where o.product.id = :productId and o.customer.id = :customerId order by o.bid desc", Orders.class)
                .setParameter("productId", productId)
                .setParameter("customerId", customerId)
                .getResultList();


        return orders.stream().findAny();
    }
}

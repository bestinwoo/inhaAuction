package project.inhaAuction.chat.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.inhaAuction.chat.domain.ChatRoom;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {
    private final EntityManager em;

    public Long save(ChatRoom chatRoom) {
        em.persist(chatRoom);
        return chatRoom.getId();
    }

    public Optional<ChatRoom> findById(Long id) {
        return Optional.ofNullable(em.find(ChatRoom.class, id));
    }

    public Optional<ChatRoom> findByMemberAndProduct(Long customerId, Long sellerId, Long productId) {
        return em.createQuery("select r from ChatRoom r where r.customer.id = :customerId and r.seller.id = :sellerId and r.product.id = :productId", ChatRoom.class)
                .setParameter("customerId", customerId)
                .setParameter("sellerId", sellerId)
                .setParameter("productId", productId)
                .getResultList().stream().findFirst();
    }

    public List<ChatRoom> findListByMemberId(Long id) {
        return em.createQuery("select r from ChatRoom r where r.customer.id = :id or r.seller.id = :id", ChatRoom.class)
                .setParameter("id", id)
                .getResultList();
    }
}

package project.inhaAuction.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.inhaAuction.notification.domain.Notification;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {
    private final EntityManager em;

    public Notification save(Notification notification) {
        em.persist(notification);
        return notification;
    }

    public List<Notification> findByMemberId(Long id) {
        return em.createQuery("select n from Notification n where n.member.id = :id", Notification.class)
                .setParameter("id", id)
                .getResultList();
    }
}

package project.inhaAuction.chat.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.inhaAuction.chat.domain.Message;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MessageRepository {
    private final EntityManager em;

    public void save(Message message) {
        em.persist(message);
    }
}

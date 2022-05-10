package project.inhaAuction.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class AdminRepository {
    private final EntityManager em;

}

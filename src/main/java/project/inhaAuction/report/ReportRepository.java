package project.inhaAuction.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.inhaAuction.report.domain.Report;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportRepository {
    private final EntityManager em;

    public Report save(Report report) {
        em.persist(report);
        return report;
    }

    public List<Report> findAll() {
        return em.createQuery("select r from Report r", Report.class)
                .getResultList();
    }
}

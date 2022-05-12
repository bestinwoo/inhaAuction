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

    public List<Report> findByPage(int page, int per_page) {
        return em.createQuery("select r from Report r", Report.class)
                .setFirstResult((page - 1) * per_page)
                .setMaxResults(per_page)
                .getResultList();
    }

    public Integer getReportCount() {
        return Integer.parseInt(em.createQuery("select count(r) from Report r")
                .getSingleResult()
                .toString());
    }

}

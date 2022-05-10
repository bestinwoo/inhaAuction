package project.inhaAuction.auth.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.inhaAuction.auth.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberRepository {
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public List<Member> findByKeyword(String keyword, int page, int per_page) {
        return em.createQuery("select m from Member m where (:keyword is null or m.loginId like :keyword)", Member.class)
                .setParameter("keyword", keyword)
                .setFirstResult((page - 1) * per_page)
                .setMaxResults(per_page)
                .getResultList();
    }

    public Integer getMemberCount(String keyword) {
        return Integer.parseInt(em.createQuery("select count(m) from Member m where (:keyword is null or m.loginId like :keyword)")
                .setParameter("keyword", keyword)
                .getSingleResult()
                .toString());
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public Optional<Member> findByEmail(String email) {
        List<Member> members = em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();

        return members.stream().findAny();
    }

    public Optional<Member> findByLoginId(String loginId) {
        List<Member> members = em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();

        return members.stream().findAny();
    }

    public void delete(Member member) {
        em.remove(member);
    }





}

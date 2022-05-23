package project.inhaAuction.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.auth.domain.Role;
import project.inhaAuction.auth.dto.MemberDto;
import project.inhaAuction.auth.repository.MemberRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MemberDto.Response> getMembers(String keyword, int page, int per_page) {
        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }

        List<Member> members = memberRepository.findByKeyword(keyword, page, per_page);
        return members.stream().map(MemberDto.Response::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Integer getMembersCount(String keyword) {
        return memberRepository.getMemberCount(keyword);
    }

    @Transactional(rollbackFor = Exception.class)
    public void modifyMemberState(Map<Long, Role> map) throws IllegalStateException {
        for (Map.Entry<Long,Role> entry: map.entrySet()) {
            Optional<Member> member = memberRepository.findById(entry.getKey());
            member.ifPresentOrElse(m -> {
                m.modifyState(entry.getValue());
            }, () -> {
                throw new IllegalStateException("존재하지 않는 회원입니다.");
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void memberBan(Map<Long, Date> map) throws IllegalStateException {
        for (Map.Entry<Long,Date> entry: map.entrySet()) {
            Optional<Member> member = memberRepository.findById(entry.getKey());
            member.ifPresentOrElse(m -> {
                m.setBanDate(entry.getValue());
            }, () -> {
                throw new IllegalStateException("존재하지 않는 회원입니다.");
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMember(Long id) throws IllegalStateException {
        Optional<Member> member = memberRepository.findById(id);
        member.ifPresentOrElse(m -> {
            memberRepository.delete(m);
        }, () -> {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        });
    }
}

package project.inhaAuction.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.auth.dto.MemberDto;
import project.inhaAuction.auth.repository.MemberRepository;

import java.util.List;
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
}

package project.inhaAuction.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.inhaAuction.auth.domain.Member;

import java.util.Date;

@Getter
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String name;
    private String email;
    private String department;
    private String state;
    private Date banDate;
    private String loginId;
    private String address;
    private String phone;

    public static MemberDto of(Member member) {
        return new MemberDto(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getDepartment(),
                member.getState(),
                member.getBanDate(),
                member.getLoginId(),
                member.getAddress(),
                member.getPhone()
                );
    }
}

package project.inhaAuction.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.auth.domain.Role;

import java.util.Date;


public class MemberDto {
    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private String email;
        private String department;
        private Role state;
        private Date banDate;
        private String loginId;
        private String address;
        private String phone;

        public static Response of(Member member) {
            return new Response(
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

    @Getter
    public static class changePassword {
        private Long id;
        private String currentPassword;
        private String newPassword;
    }

    @Getter
    public static class modifyInfo {
        private String email;
        private String address;
        private String phone;
    }

}

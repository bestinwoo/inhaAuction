package project.inhaAuction.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.auth.domain.Role;

import java.util.Date;


public class MemberDto {
    @Getter
    @Builder
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
            return Response.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .email(member.getEmail())
                    .department(member.getDepartment())
                    .state(member.getState())
                    .banDate(member.getBanDate())
                    .loginId(member.getLoginId())
                    .address(member.getAddress())
                    .phone(member.getPhone())
                    .build();
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

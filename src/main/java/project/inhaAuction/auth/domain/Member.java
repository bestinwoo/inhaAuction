package project.inhaAuction.auth.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@DynamicInsert
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "department")
    private String department;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ROLE state;

    @Column(name = "ban_date")
    private Date banDate;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void modifyInfo(String email, String address, String phone) {
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public void modifyState(ROLE state) {
        this.state = state;
    }
}

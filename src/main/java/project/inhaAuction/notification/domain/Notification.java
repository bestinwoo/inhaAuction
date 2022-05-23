package project.inhaAuction.notification.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.notification.NotificationType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@DynamicInsert
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean viewYn;
    private LocalDateTime publishDate;

    public void setViewYn(boolean viewYn) {
        this.viewYn = viewYn;
    }
}

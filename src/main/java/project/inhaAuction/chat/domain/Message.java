package project.inhaAuction.chat.domain;

import lombok.Getter;
import project.inhaAuction.auth.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private LocalDateTime sendTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

}

package project.inhaAuction.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.auth.repository.MemberRepository;
import project.inhaAuction.jwt.SecurityUtil;
import project.inhaAuction.notification.domain.Notification;
import project.inhaAuction.notification.dto.NotificationDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @Transactional(rollbackFor = Exception.class)
    public void sendNotification(NotificationDto.Post post) throws IllegalStateException {
        Notification notification = post.toNotification();
       /* Optional<Member> receiver = memberRepository.findById(post.getReceiverId());
        receiver.ifPresentOrElse(m -> {
            notification.setMember(m);
        }, () -> {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        });*/

        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationDto.Response> getNotifications(Long memberId) throws SecurityException {
        if(!SecurityUtil.getCurrentMemberId().equals(memberId)) {
            throw new SecurityException("인증정보와 요청정보가 다릅니다.");
        }
        List<Notification> notifications = notificationRepository.findByMemberId(memberId);

        return notifications.stream().map(NotificationDto.Response::of).collect(Collectors.toList());

    }
}

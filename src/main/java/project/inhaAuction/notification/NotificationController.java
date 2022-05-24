package project.inhaAuction.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import project.inhaAuction.common.BasicResponse;
import project.inhaAuction.common.ErrorResponse;
import project.inhaAuction.common.Result;
import project.inhaAuction.notification.dto.NotificationDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class NotificationController {
    private final NotificationService notificationService;
    private final SimpMessageSendingOperations messagingTemplate;

    @PostMapping("/notification")
    public ResponseEntity<BasicResponse> sendNotification(@RequestBody NotificationDto.Post post) {
    //    try {
            notificationService.sendNotification(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Result<>("알림 전송 완료"));
    /*    } catch(IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "400"));
        }*/
    }

    @GetMapping("/notification/{id}")
    public ResponseEntity<BasicResponse> getNotifications(@PathVariable Long id)
    {
        try {
            List<NotificationDto.Response> notifications = notificationService.getNotifications(id);
            if (notifications.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("받은 알림이 없습니다."));
            }
            return ResponseEntity.ok(new Result<>(notifications));
        } catch(SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(e.getMessage(), "403"));
        }
    }

    @MessageMapping("/notification/send")
    public void message(NotificationDto.Post post) {
        post.setPublishDate(LocalDateTime.now());
        Long notifyId = notificationService.sendNotification(post);
        post.setId(notifyId);
        messagingTemplate.convertAndSend("/topic/notify/" + post.getReceiverId(), post);
    }

    @PostMapping("/notification/{id}")
    public ResponseEntity<BasicResponse> viewCheck(@PathVariable Long id) {
        try {
            notificationService.viewCheck(id);
            return ResponseEntity.ok(new Result<>("알림 읽음처리 완료"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "400"));
        }
    }
    
    @DeleteMapping("/notification/{id}")
    public ResponseEntity<BasicResponse> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(new Result<>("알림 삭제 완료"));
    }
}

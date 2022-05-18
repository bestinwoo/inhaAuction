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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@CrossOrigin
public class NotificationController {
    private final NotificationService notificationService;
    private final SimpMessageSendingOperations messagingTemplate;

    @PostMapping()
    public ResponseEntity<BasicResponse> sendNotification(@RequestBody NotificationDto.Post post) {
    //    try {
            notificationService.sendNotification(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Result<>("알림 전송 완료"));
    /*    } catch(IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "400"));
        }*/
    }

    @GetMapping("/{id}")
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

    @MessageMapping("/{id}")
    public void message(@DestinationVariable("id") Long id) {
        messagingTemplate.convertAndSend("/topic/" + id, "notify socket connection completed");
    }
}

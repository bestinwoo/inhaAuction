package project.inhaAuction.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.notification.domain.Notification;

import java.time.LocalDateTime;

public class NotificationDto {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class Post {
        private String message;
        private String type;
        private Long receiverId;

        public Notification toNotification() {
            return Notification.builder()
                    .member(Member.builder().id(receiverId).build())
                    .message(message)
                    .type(type)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String message;
        private String type;
        private LocalDateTime publishDate;
        private Boolean viewYn;

        public static Response of(Notification notification) {
            return Response.builder()
                    .id(notification.getId())
                    .message(notification.getMessage())
                    .type(notification.getType())
                    .publishDate(notification.getPublishDate())
                    .viewYn(notification.isViewYn())
                    .build();
        }
    }
}

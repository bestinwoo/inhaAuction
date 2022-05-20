package project.inhaAuction.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.auth.dto.MemberDto;
import project.inhaAuction.chat.domain.ChatRoom;
import project.inhaAuction.chat.domain.Message;

import java.time.LocalDateTime;

public class MessageDto {
    @Getter
    public static class Send {
        private String message;
        private Long senderId;
        private Long roomId;

        public Message toMessage() {
            return Message.builder()
                    .message(message)
                    .sender(Member.builder().id(senderId).build())
                    .chatRoom(ChatRoom.builder().id(roomId).build())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String message;
        private MemberDto.Response sender;
        private LocalDateTime sendTime;

        public static Response of(Message message) {
            return Response.builder()
                    .message(message.getMessage())
                    .sender(MemberDto.Response.of(message.getSender()))
                    .sendTime(message.getSendTime())
                    .build();
        }
    }
}

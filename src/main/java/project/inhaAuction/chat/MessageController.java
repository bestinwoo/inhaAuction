package project.inhaAuction.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import project.inhaAuction.chat.dto.ChatRoomDto;
import project.inhaAuction.chat.dto.MessageDto;
import project.inhaAuction.chat.service.ChatRoomService;
import project.inhaAuction.chat.service.MessageService;
import project.inhaAuction.common.BasicResponse;
import project.inhaAuction.common.ErrorResponse;
import project.inhaAuction.common.Result;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class MessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    @MessageMapping("/chat/send")
    public void chat(MessageDto.Send message) {
        messageService.sendMessage(message);
        messagingTemplate.convertAndSend("/topic/chat/" + message.getReceiverId(), message);
    }

    @PostMapping("/chat/room")
    public ResponseEntity<BasicResponse> JoinChatRoom(@RequestBody ChatRoomDto.Request dto) {
        try {
            Long roomId = chatRoomService.joinChatRoom(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Result<>(roomId));
        } catch(IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "400"));
        }
    }

    @GetMapping("/chat/room")
    public ResponseEntity<BasicResponse> getChatRoomList(@RequestParam Long memberId) {
        return ResponseEntity.ok(new Result<>(chatRoomService.getRoomList(memberId)));
    }

}

package project.inhaAuction.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.inhaAuction.chat.domain.ChatRoom;
import project.inhaAuction.chat.dto.ChatRoomDto;
import project.inhaAuction.chat.repository.ChatRoomRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional(rollbackFor = Exception.class)
    public Long joinChatRoom(ChatRoomDto.Request dto) throws IllegalStateException {
        if(dto.getCustomerId().equals(dto.getSellerId())) {
            throw new IllegalStateException("자신과의 채팅방은 만들 수 없습니다.");
        }
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByMemberAndProduct(dto.getCustomerId(), dto.getSellerId(), dto.getProductId());
        if(chatRoom.isPresent()) {
            return chatRoom.get().getId();
        } else {
            return chatRoomRepository.save(dto.toChatRoom());
        }
    }

    @Transactional(readOnly = true)
    public List<ChatRoomDto.Response> getRoomList(Long memberId) {
        return chatRoomRepository.findListByMemberId(memberId).stream().map(ChatRoomDto.Response::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChatRoomDto.Response getRoomDetail(Long roomId) {
        Optional<ChatRoomDto.Response> room = chatRoomRepository.findById(roomId).map(ChatRoomDto.Response::of);
        return room.orElseThrow();
    }
}

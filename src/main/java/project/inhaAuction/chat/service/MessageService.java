package project.inhaAuction.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.inhaAuction.chat.dto.MessageDto;
import project.inhaAuction.chat.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    @Transactional(rollbackFor = Exception.class)
    public void sendMessage(MessageDto.Send message) {
        messageRepository.save(message.toMessage());
    }

}

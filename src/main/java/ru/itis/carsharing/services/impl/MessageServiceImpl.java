package ru.itis.carsharing.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.carsharing.dto.MessageDto;
import ru.itis.carsharing.models.Message;
import ru.itis.carsharing.models.User;
import ru.itis.carsharing.repositories.MessageRepository;
import ru.itis.carsharing.services.MessageService;

import java.util.List;

@Component
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<MessageDto> getMessageForUser(User user) {
        return MessageDto.from(messageRepository.getMessageForUser(user));
    }

    @Override
    public void save(MessageDto message, User user) {

        Message message1 = Message.builder()
                .fromUser(user)
                .pageId(message.getPageId())
                .text(message.getText())
                .build();

        messageRepository.save(message1);
    }
}

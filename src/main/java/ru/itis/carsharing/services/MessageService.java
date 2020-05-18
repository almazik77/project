package ru.itis.carsharing.services;

import ru.itis.carsharing.dto.MessageDto;
import ru.itis.carsharing.models.User;

import java.util.List;

public interface MessageService {
    List<MessageDto> getMessageForUser(User user);

    void save(MessageDto message, User user);
}

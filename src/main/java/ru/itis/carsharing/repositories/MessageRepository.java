package ru.itis.carsharing.repositories;

import ru.itis.carsharing.models.Message;
import ru.itis.carsharing.models.User;

import java.util.List;

public interface MessageRepository extends CrudRepository<Long, Message>{
    List<Message> getMessageForUser(User user);
}

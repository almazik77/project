package ru.itis.carsharing.controllers;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itis.carsharing.dto.MessageDto;
import ru.itis.carsharing.models.User;
import ru.itis.carsharing.security.details.UserDetailsImpl;
import ru.itis.carsharing.services.MessageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class MessagesController {
    private static final Map<String, List<MessageDto>> messages = new HashMap<>();

    @Autowired
    private MessageService messageService;

    // получили сообщение от какой либо страницы - мы его разошлем во все другие страницы
    @PostMapping("/messages")
    public ResponseEntity<Object> receiveMessage(@RequestBody MessageDto message, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        System.out.println("message : " + message.toString());
        messageService.save(message, user);

        // если сообщений с этой или для этой страницы еще не было
        if (!messages.containsKey(message.getPageId())) {
            // добавляем эту страницу в Map-у страниц
            messages.put(message.getPageId(), new ArrayList<>());
        }
        // полученное сообщение добавляем для всех открытых страниц нашего приложения
        for (List<MessageDto> pageMessages : messages.values()) {
            // перед тем как положить сообщение для какой-либо страницы
            // мы список сообщений блокируем
            synchronized (pageMessages) {
                // добавляем сообщение
                pageMessages.add(message);
                // говорим, что другие потоки могут воспользоваться этим списком
                pageMessages.notifyAll();
            }
        }

        return ResponseEntity.ok().build();
    }

    // получить все сообщения для текущего запроса
    @SneakyThrows
    @GetMapping("/messages")
    public ResponseEntity<List<MessageDto>> getMessagesForPage(@RequestParam("pageId") String pageId) {
        // получили список сообшений для страницы и заблокировали его
        System.out.println("get messages " + pageId);
        synchronized (messages.get(pageId)) {
            // если нет сообщений уходим в ожидание
            if (messages.get(pageId).isEmpty()) {
                messages.get(pageId).wait();
            }
        }

        // если сообщения есть - то кладем их в новых список
        List<MessageDto> response = new ArrayList<>(messages.get(pageId));
        // удаляем сообщения из мапы
        messages.get(pageId).clear();
        return ResponseEntity.ok(response);
    }
}
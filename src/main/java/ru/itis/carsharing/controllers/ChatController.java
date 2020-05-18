package ru.itis.carsharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.carsharing.models.User;
import ru.itis.carsharing.security.details.UserDetailsImpl;
import ru.itis.carsharing.services.MessageService;

import java.util.UUID;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/chat")
    public String getChatPage(Model model, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        model.addAttribute("messages", messageService.getMessageForUser(user));
        model.addAttribute("pageId", UUID.randomUUID().toString());
        return "chat";
    }
}

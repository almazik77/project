package ru.itis.carsharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itis.carsharing.dto.UserDto;
import ru.itis.carsharing.security.details.UserDetailsImpl;
import ru.itis.carsharing.services.UserService;


@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String getProfilePage(Authentication authentication, Model model) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return "redirect:/profile/" + userDetails.getUser().getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/{user-id}")
    public String getProfilePageOf(@PathVariable("user-id") Long userId, Model model) {
        UserDto user = userService.findOne(userId);
        model.addAttribute("user", user);
        return "profile";
    }

}

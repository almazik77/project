package ru.itis.carsharing.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignInController {

    @PreAuthorize("permitAll()")
    @GetMapping("/signIn")
    public String getSignInPage() {
        return "sign-in";
    }


}

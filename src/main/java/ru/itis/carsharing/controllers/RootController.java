package ru.itis.carsharing.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    //    @PreAuthorize("isAuthenticated()")
    @GetMapping("/")
    public String getIndexPage() {
        return "index";
    }
}

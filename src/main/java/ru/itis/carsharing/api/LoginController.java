package ru.itis.carsharing.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.carsharing.dto.TokenDto;
import ru.itis.carsharing.form.SignInForm;
import ru.itis.carsharing.services.UserService;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/login")
    public ResponseEntity<TokenDto> login(@RequestBody SignInForm signInForm) {
        return ResponseEntity.ok(userService.login(signInForm));
    }
}

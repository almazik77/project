package ru.itis.carsharing.services;

import ru.itis.carsharing.dto.TokenDto;
import ru.itis.carsharing.dto.UserDto;
import ru.itis.carsharing.form.SignInForm;
import ru.itis.carsharing.form.SignUpForm;
import ru.itis.carsharing.models.User;

import java.util.List;

public interface UserService {
    TokenDto login(SignInForm loginForm);

    User signUp(SignUpForm userForm);

    List<UserDto> findAll();

    UserDto findOne(Long userId);

    void confirm(String confirmCode);
}

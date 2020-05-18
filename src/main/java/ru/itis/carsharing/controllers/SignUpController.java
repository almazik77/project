package ru.itis.carsharing.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.carsharing.form.SignUpForm;
import ru.itis.carsharing.models.User;
import ru.itis.carsharing.services.UserFileInfoService;
import ru.itis.carsharing.services.UserService;

import javax.validation.Valid;

@Controller
@Slf4j
public class SignUpController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserFileInfoService userFileInfoService;

    @PreAuthorize("anonymous")
    @GetMapping("/signUp")
    public String getSignUpPage(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "sign-up";
    }

    @PreAuthorize("anonymous")
    @PostMapping("/signUp")
    public String signUp(@Valid SignUpForm signUpForm,
                         BindingResult bindingResult,
                         Model model,
                         MultipartFile[] uploadingFiles) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("signUpForm", signUpForm);
            return "sign-up";
        }
        log.info("sign up with" + uploadingFiles);
        User user = userService.signUp(signUpForm);
        if (uploadingFiles.length > 0)
            userFileInfoService.save(user, uploadingFiles);
        return "redirect:/signIn";
    }
}

package ru.itis.carsharing.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import ru.itis.carsharing.form.SignUpForm;
import ru.itis.carsharing.models.Mail;
import ru.itis.carsharing.models.User;
import ru.itis.carsharing.repositories.UserRepository;
import ru.itis.carsharing.services.MailService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

@Aspect
@Slf4j
public class SignUpAspect {
    @Autowired
    private MailService mailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExecutorService executorService;

    @After("execution(* ru.itis.carsharing.services.impl.UserServiceImpl.signUp(..))")
    public void afterReturning(JoinPoint joinPoint) {
        SignUpForm form = (SignUpForm) joinPoint.getArgs()[0];
        Optional<User> userCandidate = userRepository.find(form.getEmail());
        log.info("sign Up" + form.getEmail());

        if (userCandidate.isPresent()) {
            User user = userCandidate.get();
            executorService.submit(() -> {
                Map<String, String> model = new HashMap<>();

                model.put("name", user.getEmail());
                model.put("link", "http://localhost:8080/confirm/" + user.getConfirmString());


                Mail mail = Mail.builder()
                        .subject("Registration")
                        .to(user.getEmail())
                        .model(model)
                        .build();

                mailService.sendConfirmEmail(mail);
            });
        } else {
            throw new IllegalArgumentException("Не удалось зарегестрировать пользователя???");
        }
    }
}

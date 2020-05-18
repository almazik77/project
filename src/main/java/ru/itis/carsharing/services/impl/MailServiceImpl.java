package ru.itis.carsharing.services.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ru.itis.carsharing.dto.FileInfoDto;
import ru.itis.carsharing.models.Mail;
import ru.itis.carsharing.models.User;
import ru.itis.carsharing.repositories.UserRepository;
import ru.itis.carsharing.services.MailService;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender sender;

    @Autowired
    @Qualifier("username")
    private String username;

    @Autowired
    @Qualifier("getFreeMarkerConfigurationForMail")
    private Configuration freemarkerConfig;


    private void sendEmail(Mail mail, Template template) {
        try {
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());

            MimeMessage message = sender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setSubject(mail.getSubject(), "UTF-8");

            helper.setTo(mail.getTo());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(new InternetAddress(username));


            sender.send(message);


        } catch (MessagingException | IOException | TemplateException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void sendConfirmEmail(Mail mail) {
        try {
            Template template = freemarkerConfig.getTemplate("confirm-template.ftl");
            sendEmail(mail, template);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void uploadNotify(FileInfoDto fileInfoDto) {
        try {
            Optional<User> userCandidate = userRepository.find(fileInfoDto.getOwnerId());
            if (userCandidate.isPresent()) {
                Map<String, String> model = new HashMap<>();
                model.put("link", "http://localhost:8080/files/" + fileInfoDto.getStorageFileName());

                User user = userCandidate.get();
                Mail mail = Mail.builder()
                        .to(user.getEmail())
                        .subject("uploading file")
                        .model(model)
                        .build();
                Template template = freemarkerConfig.getTemplate("upload-template.ftl");
                sendEmail(mail, template);

            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

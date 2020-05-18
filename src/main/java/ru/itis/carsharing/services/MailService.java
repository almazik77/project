package ru.itis.carsharing.services;


import ru.itis.carsharing.dto.FileInfoDto;
import ru.itis.carsharing.models.Mail;

public interface MailService {
    void sendConfirmEmail(Mail mail);

    void uploadNotify(FileInfoDto fileInfoDto);
}

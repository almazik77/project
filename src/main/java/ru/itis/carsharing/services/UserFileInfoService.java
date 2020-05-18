package ru.itis.carsharing.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.carsharing.models.User;

public interface UserFileInfoService {
    void save(User user, MultipartFile[] multipartFiles);
}

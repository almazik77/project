package ru.itis.carsharing.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.carsharing.models.Car;
import ru.itis.carsharing.models.FileInfo;

import javax.servlet.http.HttpServletResponse;

public interface FilesService {
    FileInfo save(MultipartFile file);

    void writeFileToResponse(String fileName, HttpServletResponse response);
}

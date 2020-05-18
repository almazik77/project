package ru.itis.carsharing.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.carsharing.models.Car;

public interface CarFileInfoService {

    void save(MultipartFile[] multipartFiles, Car car);

}

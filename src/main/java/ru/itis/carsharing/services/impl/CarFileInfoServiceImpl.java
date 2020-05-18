package ru.itis.carsharing.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.carsharing.models.Car;
import ru.itis.carsharing.models.CarFileInfo;
import ru.itis.carsharing.models.FileInfo;
import ru.itis.carsharing.repositories.CarFileInfoRepository;
import ru.itis.carsharing.services.CarFileInfoService;
import ru.itis.carsharing.services.FilesService;

import java.util.Arrays;

@Service
public class CarFileInfoServiceImpl implements CarFileInfoService {
    @Autowired
    private CarFileInfoRepository carFileInfoRepository;

    @Autowired
    private FilesService filesService;

    @Override
    public void save(MultipartFile[] multipartFiles, Car car) {
        Arrays.stream(multipartFiles).forEach(multipartFile -> {
            FileInfo fileInfo = filesService.save(multipartFile);
            CarFileInfo carFileInfo = CarFileInfo.builder()
                    .fileInfo(fileInfo)
                    .car(car)
                    .build();
            carFileInfoRepository.save(carFileInfo);
        });
    }
}

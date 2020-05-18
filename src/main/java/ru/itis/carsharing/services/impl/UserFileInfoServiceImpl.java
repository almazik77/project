package ru.itis.carsharing.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.carsharing.models.FileInfo;
import ru.itis.carsharing.models.User;
import ru.itis.carsharing.models.UserFileInfo;
import ru.itis.carsharing.repositories.UserFileInfoRepository;
import ru.itis.carsharing.services.FilesService;
import ru.itis.carsharing.services.UserFileInfoService;

import java.util.Arrays;

@Service
public class UserFileInfoServiceImpl implements UserFileInfoService {
    @Autowired
    private UserFileInfoRepository userFileInfoRepository;

    @Autowired
    private FilesService filesService;

    @Override
    public void save(User user, MultipartFile[] multipartFiles) {
        Arrays.stream(multipartFiles).forEach(multipartFile -> {
            FileInfo fileInfo = filesService.save(multipartFile);
            UserFileInfo userFileInfo = UserFileInfo.builder()
                    .fileInfo(fileInfo)
                    .user(user)
                    .build();
            userFileInfoRepository.save(userFileInfo);
        });
    }
}

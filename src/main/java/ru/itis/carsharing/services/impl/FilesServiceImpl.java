package ru.itis.carsharing.services.impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.carsharing.models.Car;
import ru.itis.carsharing.models.FileInfo;
import ru.itis.carsharing.repositories.FileStorageRepository;
import ru.itis.carsharing.services.FilesService;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class FilesServiceImpl implements FilesService {
    @Autowired
    private String fileStoragePath;

    @Autowired
    private FileStorageRepository fileStorageRepository;


    @Override
    public FileInfo save(MultipartFile file) {
        String storageFileName = createStorageName(file.getOriginalFilename());
        FileInfo fileInfo = FileInfo.builder()
                .originalFileName(file.getOriginalFilename())
                .storageFileName(storageFileName)
                .size(file.getSize())
                .type(file.getContentType())
                .url(fileStoragePath + "\\" + storageFileName)
                .build();


        fileStorageRepository.save(fileInfo);

        try {
            Files.copy(file.getInputStream(), Paths.get(fileStoragePath, storageFileName));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        return fileInfo;
    }


    @Override
    public void writeFileToResponse(String fileName, HttpServletResponse response) {
        FileInfo fileInfo = get(fileName);
        response.setContentType(fileInfo.getType());
        try {
            InputStream inputStream = new FileInputStream(new java.io.File(fileInfo.getUrl()));
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (
                IOException e) {
            throw new IllegalArgumentException(e);
        }

    }


    public FileInfo get(String name) {
        Optional<FileInfo> fileInfoCandidate = fileStorageRepository.findOneByStorageFileName(name);
        return fileInfoCandidate.orElse(null);
    }

    private String createStorageName(String originalFileName) {
        String extension = FilenameUtils.getExtension(originalFileName);
        String newFileName = UUID.randomUUID().toString();
        return newFileName + "." + extension;
    }
}

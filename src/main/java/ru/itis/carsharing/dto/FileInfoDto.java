package ru.itis.carsharing.dto;

import lombok.*;
import ru.itis.carsharing.models.FileInfo;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder()
public class FileInfoDto {
    private Long id;
    // название файла в хранилище
    private String storageFileName;
    // название файла оригинальное
    private String originalFileName;
    // размер файла
    private Long size;
    // тип файла (MIME)
    private String type;
    // по какому URL можно получить файл
    private String url;


    private Long ownerId;

    public static FileInfoDto from(FileInfo fileInfo) {
        return FileInfoDto.builder()
                .url(fileInfo.getUrl())
                .type(fileInfo.getType())
                .size(fileInfo.getSize())
                .storageFileName(fileInfo.getStorageFileName())
                .originalFileName(fileInfo.getOriginalFileName())
                .build();
    }
}

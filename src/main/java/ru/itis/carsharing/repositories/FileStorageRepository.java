package ru.itis.carsharing.repositories;

import ru.itis.carsharing.models.FileInfo;

import java.util.Optional;
import java.util.Set;

public interface FileStorageRepository extends CrudRepository<Long, FileInfo> {
    Optional<FileInfo> find(String name);

    Optional<FileInfo> findOneByStorageFileName(String storageName);

    Set<FileInfo> findByCarId(Long id);
}

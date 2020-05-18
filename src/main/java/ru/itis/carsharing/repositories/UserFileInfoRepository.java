package ru.itis.carsharing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.carsharing.models.UserFileInfo;

@Repository
public interface UserFileInfoRepository extends JpaRepository<UserFileInfo, Long> {
}

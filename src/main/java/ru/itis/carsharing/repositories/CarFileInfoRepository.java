package ru.itis.carsharing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.carsharing.models.CarFileInfo;

@Repository
public interface CarFileInfoRepository extends JpaRepository<CarFileInfo, Long> {
}

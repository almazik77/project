package ru.itis.carsharing.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itis.carsharing.dto.CarDto;
import ru.itis.carsharing.models.Car;
import ru.itis.carsharing.repositories.CarRepository;
import ru.itis.carsharing.services.CarService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    private CarRepository carRepository;

    public static final double R = 6371;  // Earth's radius

    @Override
    public List<CarDto> findAll() {
        return CarDto.from(carRepository.findAll());
    }

    @Override
    public void save(Car car) {
        carRepository.save(car);
    }

    @Override
    public CarDto find(Long carId) {
        Optional<Car> car = carRepository.find(carId);
        return CarDto.from(car.get());
    }

    @Override
    public List<CarDto> findNear(Double x, Double y) {
        List<Car> allCars = carRepository.findAll();
        return CarDto.from(allCars.stream()
                .filter(car -> checkIfNear(x, y, car))
                .collect(Collectors.toList()));
    }

    @Override
    public List<CarDto> findAll(Long pageNum, Long pageSize) {
        return CarDto.from(carRepository.findAll(pageNum, pageSize));
    }

    private boolean checkIfNear(Double x, Double y, Car car) {
        double lat1 = car.getLtd();
        double lat2 = y;
        double lon1 = car.getLng();
        double lon2 = x;
        double sin1 = Math.sin((lat1 - lat2) / 2);
        double sin2 = Math.sin((lon1 - lon2) / 2);
        return 2 * R * Math.asin(Math.sqrt(sin1 * sin1 + sin2 * sin2 * Math.cos(lat1) * Math.cos(lat2))) <= 1e3;
    }
}

package ru.itis.carsharing.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.carsharing.dto.CarDto;
import ru.itis.carsharing.services.CarService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CarRestController {
    @Autowired
    private CarService carService;

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cars")
    public List<CarDto> getCarList() {
        return carService.findAll();
    }
}

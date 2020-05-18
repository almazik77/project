package ru.itis.carsharing.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.carsharing.dto.CarDto;
import ru.itis.carsharing.services.CarService;

import java.util.List;

@RestController
public class CarsController {

    @Autowired
    private CarService carService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/rest/cars")
    public List<CarDto> getCarList(@RequestParam(required = false) Double x,
                                   @RequestParam(required = false) Double y) {
        return carService.findAll();
    }

}

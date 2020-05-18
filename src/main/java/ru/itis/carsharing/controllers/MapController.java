package ru.itis.carsharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.carsharing.services.CarService;

@Controller
public class MapController {

    @Autowired
    private CarService carService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/map")
    public String getMapPage(Model model) {
        return "map";
    }

}

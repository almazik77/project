package ru.itis.carsharing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.carsharing.dto.CarDto;
import ru.itis.carsharing.form.AddCarForm;
import ru.itis.carsharing.form.OrderForm;
import ru.itis.carsharing.models.Car;
import ru.itis.carsharing.models.User;
import ru.itis.carsharing.security.details.UserDetailsImpl;
import ru.itis.carsharing.services.CarFileInfoService;
import ru.itis.carsharing.services.CarService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarFileInfoService carFileInfoService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cars/add")
    public String getCarrAddPage(Model model) {
        model.addAttribute("addCarForm", new AddCarForm());
        return "car-add";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cars/add")
    public String addCar(Authentication authentication,
                         @RequestParam("uploadingFiles") MultipartFile[] uploadingFiles,
                         @Valid AddCarForm addCarForm,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("addCarForm", addCarForm);
            return "car-add";
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Double[] coords =
                Arrays.stream(addCarForm.getCoordinates()
                        .replace("[", "")
                        .replace("]", "")
                        .split(","))
                        .map(Double::parseDouble)
                        .toArray(Double[]::new);
        Car car = Car.builder()
                .model(addCarForm.getModel())
                .cost(addCarForm.getCost())
                .lng(coords[0])
                .ltd(coords[1])
                .owner(user)
                .build();

        carService.save(car);

        carFileInfoService.save(uploadingFiles, car);

        return "redirect:/cars/" + car.getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cars")
    public String getCarsPage(Model model,
                              @RequestParam(required = false, defaultValue = "1", name = "page") String page,
                              @RequestParam(required = false, defaultValue = "20", name = "pageCount") String pageSize) {
        Long pageL = Long.parseLong(page);
        Long pageSizeL = Long.parseLong(pageSize);
        List<CarDto> carList = carService.findAll(pageL, pageSizeL);
        model.addAttribute("carList", carList);
        return "car-list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cars/{car-id}")
    public String getCarPage(@PathVariable("car-id") Long carId, Model model) {
        CarDto carDto = carService.find(carId);
        model.addAttribute("car", carDto);
        if (!model.containsAttribute("orderForm")) {
            model.addAttribute("orderForm", new OrderForm());
        }
        return "car-profile";
    }


}

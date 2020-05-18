package ru.itis.carsharing.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itis.carsharing.dto.UserDto;
import ru.itis.carsharing.form.OrderForm;
import ru.itis.carsharing.models.User;
import ru.itis.carsharing.security.details.UserDetailsImpl;
import ru.itis.carsharing.services.CarService;
import ru.itis.carsharing.services.OrderService;
import ru.itis.carsharing.services.UserService;

import javax.validation.Valid;

@Controller
@Slf4j
public class OrderController {
    @Autowired
    private CarService carService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cars/{car-id}/order")
    public String createOrder(@PathVariable("car-id") Long carId,
                              @ModelAttribute @Valid OrderForm orderForm,
                              Authentication authentication,
                              BindingResult bindingResult,
                              Model model) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        if (!orderService.checkDates(orderForm, carId)) {
            bindingResult.rejectValue("start", "{error.incorrect.dates}");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("orderForm", orderForm);
        } else {
            orderService.createOrder(orderForm, user, carId);
        }
        return "redirect:/cars/" + carId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/orders/pay/{order-id}")
    public String payOrder(Authentication authentication, Model model, @PathVariable(name = "order-id") Long orderId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        if (orderService.pay(orderId, user))
            return "redirect:/profile";
        model.addAttribute("err", "pay err");
        return getOrders(authentication, model);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/orders")
    public String getOrders(Authentication authentication, Model model) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        return getOrdersPage(user.getId(), model);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user/{user-id}/orders")
    public String getOrdersPage(@PathVariable("user-id") Long userId, Model model) {
        UserDto userDto = userService.findOne(userId);
        model.addAttribute("user", userDto);
        log.info(userDto.getOrderList().toString());
        return "order-list";
    }
}

package ru.itis.carsharing.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.carsharing.dto.CarDto;
import ru.itis.carsharing.form.OrderForm;
import ru.itis.carsharing.models.Car;
import ru.itis.carsharing.models.Order;
import ru.itis.carsharing.models.User;
import ru.itis.carsharing.repositories.OrderRepository;
import ru.itis.carsharing.services.CarService;
import ru.itis.carsharing.services.OrderService;
import ru.itis.carsharing.services.UserService;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void createOrder(OrderForm form, User user, Long carId) {
        CarDto carDto = carService.find(carId);

        Car car = Car.builder()
                .id(carDto.getId())
                .build();

        Order order = Order.builder()
                .beginDate(form.getStart())
                .endDate(form.getEnd())
                .car(car)
                .user(user)
                .value((double) (carDto.getCost() * (ChronoUnit.DAYS.between(form.getStart(), form.getEnd()))))
                .isPayed(Boolean.FALSE)
                .build();


        orderRepository.save(order);

    }

    @Override
    public boolean checkDates(OrderForm form, Long carId) {
        Set<Order> orderSet = orderRepository.findByCarId(carId);
        for (Order order : orderSet) {
            if (order.getBeginDate().isAfter(form.getStart()) && order.getEndDate().isBefore(form.getEnd()))
                return false;
            if (order.getBeginDate().isAfter(form.getStart()) && order.getBeginDate().isBefore(form.getEnd()))
                return false;
            if (order.getEndDate().isAfter(form.getStart()) && order.getEndDate().isBefore(form.getEnd()))
                return false;
        }

        return true;
    }

    @Override
    public boolean pay(Long orderId, User user) {
        Optional<Order> orderCandidate = orderRepository.find(orderId);
        if (orderCandidate.isPresent()) {
            Order order = orderCandidate.get();
            if (order.getUser().getId().equals(user.getId())) {
                order.setPayed(true);
                orderRepository.update(order);
                return true;
            }
        }
        return false;
    }
}

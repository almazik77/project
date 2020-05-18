package ru.itis.carsharing.services;

import ru.itis.carsharing.form.OrderForm;
import ru.itis.carsharing.models.User;

public interface OrderService {
    void createOrder(OrderForm form, User user, Long carId);


    /*

    @return true, if correct dates, else false

     */
    boolean checkDates(OrderForm form, Long carId);

    boolean pay(Long orderId, User user);
}

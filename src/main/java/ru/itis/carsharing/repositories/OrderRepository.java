package ru.itis.carsharing.repositories;

import ru.itis.carsharing.models.Order;

import java.util.Set;

public interface OrderRepository extends CrudRepository<Long, Order> {
    void save(Order order);

    Set<Order> findByCarId(Long id);

    Set<Order> findByUserId(long id);
}

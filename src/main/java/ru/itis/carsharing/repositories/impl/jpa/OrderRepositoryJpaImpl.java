package ru.itis.carsharing.repositories.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.carsharing.models.Car;
import ru.itis.carsharing.models.Order;
import ru.itis.carsharing.repositories.CarRepository;
import ru.itis.carsharing.repositories.OrderRepository;
import ru.itis.carsharing.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderRepositoryJpaImpl implements OrderRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Optional<Order> find(Long id) {
        return
                Optional.of(entityManager.find(Order.class, id));
    }

    @Override
    @Transactional
    public List<Order> findAll() {
        return entityManager.createQuery("select o from Order o", Order.class).getResultList();
    }

    @Override
    @Transactional
    public void update(Order model) {
        entityManager.merge(model);
    }

    @Override
    @Transactional
    public void save(Order order) {
        entityManager.persist(order);
    }

    @Override
    @Transactional
    public Set<Order> findByCarId(Long carId) {
        TypedQuery<Order> query = entityManager.createQuery("select o from Order o where o.car = :car", Order.class);
        Optional<Car> carCandidate = carRepository.find(carId);
        if(carCandidate.isPresent()) {
            query.setParameter("car", carCandidate.get());
            return query.getResultStream().collect(Collectors.toSet());
        }
        throw new IllegalArgumentException("No car with id =" + carId);
    }

    @Override
    @Transactional
    public Set<Order> findByUserId(long id) {
        TypedQuery<Order> query = entityManager.createQuery("select o from Order o where o.user = :user", Order.class);
        query.setParameter("user", userRepository.find(id));
        return query.getResultStream().collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        entityManager.remove(find(id));
    }
}

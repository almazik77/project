package ru.itis.carsharing.repositories.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.carsharing.models.Car;
import ru.itis.carsharing.repositories.CarRepository;
import ru.itis.carsharing.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CarRepositoryJpaImpl implements CarRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public Optional<Car> find(Long id) {
        Car car = entityManager.find(Car.class, id);
        return Optional.of(car);
    }

    @Override
    @Transactional
    public List<Car> findAll() {
        return entityManager.createQuery("select c from Car c", Car.class).getResultList();
    }

    @Transactional
    @Override
    public void save(Car entity) {
        entityManager.persist(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        entityManager.remove(find(id));
    }

    @Transactional
    @Override
    public void update(Car model) {
        entityManager.merge(model);
    }

    @Override
    @Transactional
    public Set<Car> findByOwnerId(Long id) {
        TypedQuery<Car> query = entityManager.createQuery("select c from Car c where c.owner = :owner", Car.class);
        query.setParameter("owner", userRepository.find(id));
        return query.getResultStream().collect(Collectors.toSet());
    }

    @Override
    public List<Car> findAll(Long pageNum, Long pageSize) {
        TypedQuery<Car> query = entityManager.createQuery("select c from Car c", Car.class);
        query.setFirstResult((int) (pageSize*(pageNum-1)));
        query.setMaxResults(Math.toIntExact(pageSize));
        return query.getResultList();
    }


}

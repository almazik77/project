package ru.itis.carsharing.repositories;

import ru.itis.carsharing.dto.CarDto;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<ID, V> {
    Optional<V> find(ID id);

    List<V> findAll();

    void update(V model);

    void save(V entity);

    void delete(ID id);

}
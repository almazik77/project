package ru.itis.carsharing.repositories;

import ru.itis.carsharing.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Long, User> {
    Optional<User> find(String email);

    void deleteConfirmString(String string);

    Optional<User> findOneByEmail(String email);

    void confirmEmail(String confirmCode);
}

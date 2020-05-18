package ru.itis.carsharing.repositories;

import ru.itis.carsharing.models.Token;

import java.util.Optional;

public interface TokensRepository extends CrudRepository<Long, Token> {
    public void save(Token token);

    Optional<Token> findOneByValue(String name);
}

package ru.itis.carsharing.repositories.impl.jdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.itis.carsharing.models.Token;
import ru.itis.carsharing.repositories.TokensRepository;

import java.util.List;
import java.util.Optional;

public class TokensRepositoryJdbcTemplateImpl implements TokensRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public Optional<Token> find(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Token> findAll() {
        return null;
    }

    @Override
    public void update(Token model) {

    }

    @Override
    public void save(Token token) {

    }

    @Override
    public Optional<Token> findOneByValue(String name) {
        return Optional.empty();
    }

    @Override
    public void delete(Long aLong) {

    }
}

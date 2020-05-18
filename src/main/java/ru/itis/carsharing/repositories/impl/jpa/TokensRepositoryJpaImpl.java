package ru.itis.carsharing.repositories.impl.jpa;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.carsharing.models.Token;
import ru.itis.carsharing.repositories.TokensRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Component
public class TokensRepositoryJpaImpl implements TokensRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Optional<Token> find(Long id) {
        return Optional.of(entityManager.find(Token.class, id));
    }

    @Override
    @Transactional
    public List<Token> findAll() {
        return entityManager.createQuery("select t from Token t", Token.class).getResultList();
    }

    @Override
    @Transactional
    public void update(Token model) {
        entityManager.merge(model);

    }

    @Override
    @Transactional
    public void save(Token token) {
        entityManager.persist(token);
    }

    @Override
    public Optional<Token> findOneByValue(String name) {
        TypedQuery<Token> query = entityManager.createQuery("select t from Token t where t.value = :value", Token.class);
        query.setParameter("value", name);
        return Optional.of(query.getSingleResult());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        entityManager.remove(find(id));
    }
}

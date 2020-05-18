package ru.itis.carsharing.repositories.impl.jpa;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.carsharing.models.State;
import ru.itis.carsharing.models.User;
import ru.itis.carsharing.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryJpaImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Optional<User> find(String email) {
        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.email = :email", User.class);
        query.setParameter("email", email);
        return Optional.of(query.getSingleResult());
    }

    @Override
    @Transactional
    public void deleteConfirmString(String string) {
        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.confirmString = :confirmString", User.class);
        query.setParameter("confirmString", string);
        User user = query.getSingleResult();
        if (user != null) {
            user.setConfirmString("");
        }
        update(user);
    }

    @Override
    public Optional<User> findOneByEmail(String email) {
        return find(email);
    }

    @Override
    public void confirmEmail(String confirmCode) {
        TypedQuery<User> query = entityManager.createQuery("select u from  User u where u.confirmString = :cc", User.class);
        query.setParameter("cc", confirmCode);
        User user = query.getSingleResult();
        user.setState(State.CONFIRMED);
        user.setConfirmString("");
        update(user);
    }

    @Override
    @Transactional
    public Optional<User> find(Long id) {
        return Optional.of(entityManager.find(User.class, id));
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    @Transactional
    public void update(User model) {
        entityManager.merge(model);

    }

    @Override
    @Transactional
    public void save(User entity) {
        entityManager.persist(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        entityManager.remove(find(id));
    }
}

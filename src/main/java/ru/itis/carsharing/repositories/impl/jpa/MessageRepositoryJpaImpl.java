package ru.itis.carsharing.repositories.impl.jpa;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.carsharing.models.Message;
import ru.itis.carsharing.models.User;
import ru.itis.carsharing.repositories.MessageRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Component
public class MessageRepositoryJpaImpl implements MessageRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Message> getMessageForUser(User user) {
        TypedQuery<Message> query = entityManager.createQuery("select m from Message m where m.fromUser=:user", Message.class)
                .setParameter("user", user);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Optional<Message> find(Long id) {
        return Optional.ofNullable(entityManager.find(Message.class, id));
    }

    @Override
    @Transactional
    public List<Message> findAll() {
        return entityManager.createQuery("select m from Message m", Message.class).getResultList();
    }

    @Override
    @Transactional
    public void update(Message model) {
        entityManager.merge(model);
    }

    @Override
    @Transactional
    public void save(Message entity) {
        entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        entityManager.remove(find(id));
    }
}

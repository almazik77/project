package ru.itis.carsharing.repositories.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.carsharing.models.FileInfo;
import ru.itis.carsharing.repositories.CarRepository;
import ru.itis.carsharing.repositories.FileStorageRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FileStorageRepositoryJpaImpl implements FileStorageRepository {
    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    private CarRepository carRepository;

    @Override
    @Transactional
    public Optional<FileInfo> find(String name) {
        return Optional.of(entityManager.find(FileInfo.class, name));
    }

    @Override
    public Optional<FileInfo> findOneByStorageFileName(String storageName) {
        TypedQuery<FileInfo> query = entityManager.createQuery("select f from FileInfo f WHERE f.storageFileName = :storageFileName", FileInfo.class);
        query.setParameter("storageFileName", storageName);
        return Optional.of(query.getSingleResult());
    }

    @Override
    @Transactional
    public Set<FileInfo> findByCarId(Long id) {
        TypedQuery<FileInfo> query = entityManager.createQuery("select f from FileInfo f WHERE f.car = :car", FileInfo.class);
        query.setParameter("car", carRepository.find(id));
        return query.getResultStream().collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Optional<FileInfo> find(Long id) {
        return Optional.of(entityManager.find(FileInfo.class, id));
    }

    @Override
    @Transactional
    public List<FileInfo> findAll() {
        return entityManager.createQuery("select f from FileInfo f", FileInfo.class).getResultList();
    }

    @Override
    @Transactional
    public void update(FileInfo model) {
        entityManager.merge(model);

    }

    @Override
    @Transactional
    public void save(FileInfo entity) {
        entityManager.persist(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        entityManager.remove(find(id));
    }
}

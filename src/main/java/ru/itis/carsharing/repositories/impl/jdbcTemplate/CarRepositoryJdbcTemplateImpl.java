package ru.itis.carsharing.repositories.impl.jdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.itis.carsharing.models.Car;
import ru.itis.carsharing.repositories.CarRepository;
import ru.itis.carsharing.repositories.FileStorageRepository;
import ru.itis.carsharing.repositories.OrderRepository;
import ru.itis.carsharing.repositories.UserRepository;

import javax.persistence.TypedQuery;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CarRepositoryJdbcTemplateImpl implements CarRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private OrderRepository orderRepository;

    //language=SQL
    private final String SQL_INSERT_CAR = "INSERT INTO car (model, cost, owner_id) values (?, ?, ?);";
    //language=SQL
    private final String SQL_FIND_CAR_BY_ID = "SELECT * FROM car WHERE id = ?;";
    //language=SQL
    private final String SQL_FIND_ALL = "SELECT * FROM car;";
    //language=SQL
    private final String SQL_FIND_ALL_BY_USER_ID = "SELECT * FROM car where owner_id=?";


    private RowMapper<Car> carRowMapper = (row, rowNumber) -> {
        return Car.builder()
                .model(row.getString("model"))
                .id(row.getLong("id"))
                .owner(userRepository.find(Long.toString(row.getLong("owner_id"))).get())
                .orderSet(orderRepository.findByCarId(row.getLong("id")))
                .cost(row.getLong("cost"))
                .build();
    };

    @Override
    public Optional<Car> find(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_CAR_BY_ID, new Object[]{id}, carRowMapper));
    }

    @Override
    public List<Car> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, carRowMapper);
    }

    @Override
    public void update(Car entity) {
        // TODO : check if correct
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT_CAR);
            statement.setString(1, entity.getModel());
            statement.setLong(2, entity.getCost());
            statement.setLong(3, entity.getOwner().getId());
            return statement;
        }, keyHolder);

        entity.setId((Long) keyHolder.getKey());
    }

    @Override
    public void save(Car entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT_CAR);
            statement.setString(1, entity.getModel());
            statement.setLong(2, entity.getCost());
            statement.setLong(3, entity.getOwner().getId());
            return statement;
        }, keyHolder);

        entity.setId((Long) keyHolder.getKey());
    }

    @Override
    public void delete(Long aLong) {
        // TODO
    }

    @Override
    public Set<Car> findByOwnerId(Long id) {
        return new HashSet<>(jdbcTemplate.query(SQL_FIND_ALL_BY_USER_ID, new Object[]{id}, carRowMapper));
    }

    @Override
    public List<Car> findAll(Long count, Long offset) {
        //TODO
        return null;
    }

}

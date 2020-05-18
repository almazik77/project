package ru.itis.carsharing.repositories.impl.jdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.itis.carsharing.models.Order;
import ru.itis.carsharing.repositories.CarRepository;
import ru.itis.carsharing.repositories.OrderRepository;
import ru.itis.carsharing.repositories.UserRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class OrderRepositoryJdbcTemplateImpl implements OrderRepository {

    //language=SQL
    private final String SQL_INSERT_ORDER = "INSERT INTO ordr (begin_date, end_date, is_payed, user_id, car_id) VALUES (?,?,?,?,?)";
    //language=SQL
    private final String SQL_FIND_ORDER_BY_ID = "SELECT * FROM ordr WHERE id=?";
    //language=SQL
    private final String SQL_FIND_ALL = "SELECT * FROM ordr";
    //language=SQL
    private final String SQL_FIND_BY_CAR_ID = "SELECT * FROM ordr WHERE car_id=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    private RowMapper<Order> orderRowMapper = (row, rowNumber) -> {
        return Order.builder()
                .beginDate(row.getDate("begin_date").toLocalDate())
                .endDate(row.getDate("end_date").toLocalDate())
                .car(carRepository.find(row.getLong("car_id")).orElse(null))
                .user(userRepository.find(row.getLong("user_id")).orElse(null))
                .isPayed(row.getBoolean("is_payed"))
                .id(row.getLong("id"))
                .build();
    };

    @Override
    public Optional<Order> find(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_ORDER_BY_ID, new Object[]{id}, orderRowMapper));
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, orderRowMapper);
    }

    @Override
    public void update(Order model) {
        //TODO
    }

    @Override
    public void save(Order entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT_ORDER);
            statement.setDate(1, Date.valueOf(entity.getBeginDate()));
            statement.setDate(2, Date.valueOf(entity.getEndDate()));
            statement.setBoolean(3, entity.isPayed());
            statement.setLong(4, entity.getUser().getId());
            statement.setLong(5, entity.getCar().getId());
            return statement;
        }, keyHolder);

        entity.setId((Long) keyHolder.getKey());
    }


    @Override
    public Set<Order> findByCarId(Long carId) {
        return new HashSet<>(jdbcTemplate.query(SQL_FIND_BY_CAR_ID, new Object[]{carId}, orderRowMapper));
    }

    @Override
    public Set<Order> findByUserId(long id) {
        // TODO
        return null;
    }

    @Override
    public void delete(Long aLong) {
        // TODO
    }
}

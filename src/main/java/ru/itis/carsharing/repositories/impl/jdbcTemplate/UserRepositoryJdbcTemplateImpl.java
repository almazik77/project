package ru.itis.carsharing.repositories.impl.jdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.itis.carsharing.models.Role;
import ru.itis.carsharing.models.State;
import ru.itis.carsharing.models.User;
import ru.itis.carsharing.repositories.CarRepository;
import ru.itis.carsharing.repositories.OrderRepository;
import ru.itis.carsharing.repositories.UserRepository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;


public class UserRepositoryJdbcTemplateImpl implements UserRepository {


    //language=SQL
    private final String SQL_INSERT_USER = "INSERT INTO usr (firstname, secondname, email, hashpassword, confirmstring, state, role) values (?, ?, ?, ?, ?,?, ?);";
    //language=SQL
    private final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM server_user WHERE email = ?;";
    //language=SQL
    private final String SQL_FIND_USER_BY_ID = "SELECT * FROM server_user WHERE id = ?;";
    //language=SQL
    private final String SQL_FIND_ALL = "SELECT * FROM server_user;";
    //language=SQL
    private final String SQL_DELETE_CONFIRM_STRING = "UPDATE server_user SET confirm_string = '', state = 'CONFIRMED' where confirm_string = ?;";


    @Autowired
    private CarRepository carRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = (row, rowNumber) -> {
        return User.builder()
                .id(row.getLong("id"))
                .email(row.getString("email"))
                .firstName(row.getString("firstname"))
                .lastName(row.getString("lastname"))
                .confirmString(row.getString("confirmstring"))
                .role(Role.valueOf(row.getString("role")))
                .state(State.valueOf(row.getString("state")))
                .hashPassword(row.getString("hashpassword"))
                .carSet(carRepository.findByOwnerId(row.getLong("id")))
                .orderSet(orderRepository.findByUserId(row.getLong("id")))
                .build();
    };

    @Override
    public Optional<User> find(String email) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_USER_BY_EMAIL, new Object[]{email}, userRowMapper);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteConfirmString(String string) {
        jdbcTemplate.update(SQL_DELETE_CONFIRM_STRING, string);
    }

    @Override
    public Optional<User> findOneByEmail(String email) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_USER_BY_EMAIL, new Object[]{email}, userRowMapper));
    }

    @Override
    public void confirmEmail(String confirmCode) {
        deleteConfirmString(confirmCode);
    }

    @Override
    public void save(User entity) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT_USER);
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getHashPassword());
            statement.setString(5, entity.getConfirmString());
            statement.setString(6, entity.getRole().toString());
            statement.setString(7, entity.getState().toString());

            return statement;
        }, keyHolder);

        entity.setId((Long) keyHolder.getKey());
    }

    @Override
    public void update(User model) {
        // TODO
    }

    @Override
    public void delete(Long id) {
        // TODO
    }

    @Override
    public Optional<User> find(Long id) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_USER_BY_ID, new Object[]{id}, userRowMapper);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, userRowMapper);
    }
}

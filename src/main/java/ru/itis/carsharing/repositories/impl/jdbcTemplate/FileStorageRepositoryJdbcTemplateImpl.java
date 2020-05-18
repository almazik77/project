package ru.itis.carsharing.repositories.impl.jdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.itis.carsharing.models.FileInfo;
import ru.itis.carsharing.repositories.CarRepository;
import ru.itis.carsharing.repositories.FileStorageRepository;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FileStorageRepositoryJdbcTemplateImpl implements FileStorageRepository {


    //language=SQL
    private final String SQL_INSERT_FILE_INFO = "INSERT INTO file_storage_info (storage_file_name, original_file_name, size, type, url) values (?, ?, ?, ?, ?, ?);";
    //language=SQL
    private final String SQL_FIND_FILE_INFO_BY_NAME = "SELECT * FROM file_storage_info WHERE storage_file_name = ?;";
    //language=SQL
    private final String SQL_FIND_FILE_INFO_BY_ID = "SELECT * FROM file_storage_info WHERE id = ?;";
    //language=SQL
    private final String SQL_FIND_ALL = "SELECT * FROM file_storage_info;";
    //language=SQL
    private final String SQL_FIND_BY_CAR_ID = "SELECT * FROM file_storage_info where car_id=?";
    //language=SQL
    private final String SQL_FIND_BY_STORAGE_NAME = "SELECT * FROM file_storage_info where strorage_file_name=?";


    @Autowired
    private CarRepository carRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<FileInfo> fileInfoRowMapper = (row, rowNumber) -> {
        return FileInfo.builder()
                .id(row.getLong("id"))
                .originalFileName(row.getString("original_file_name"))
                .storageFileName(row.getString("storage_file_name"))
                .size(row.getLong("size"))
                .type(row.getString("type"))
                .url(row.getString("url"))
                .build();
    };

    @Override
    public Optional<FileInfo> find(Long id) {
        try {
            FileInfo fileInfo = jdbcTemplate.queryForObject(SQL_FIND_FILE_INFO_BY_ID, new Object[]{id}, fileInfoRowMapper);
            return Optional.ofNullable(fileInfo);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<FileInfo> find(String name) {
        try {
            FileInfo fileInfo = jdbcTemplate.queryForObject(SQL_FIND_FILE_INFO_BY_NAME, new Object[]{name}, fileInfoRowMapper);
            return Optional.ofNullable(fileInfo);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<FileInfo> findOneByStorageFileName(String storageName) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_BY_STORAGE_NAME, new Object[]{storageName}, fileInfoRowMapper));
    }

    @Override
    public Set<FileInfo> findByCarId(Long id) {
        return new HashSet<>(jdbcTemplate.query(SQL_FIND_BY_CAR_ID, new Object[]{id}, fileInfoRowMapper));
    }

    @Override
    public List<FileInfo> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, fileInfoRowMapper);
    }

    @Override
    public void update(FileInfo model) {
        // TODO
        Object[] params = new Object[]{model.getStorageFileName(), model.getOriginalFileName(), model.getSize(), model.getType(), model.getUrl()};
    }

    @Override
    public void save(FileInfo entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT_FILE_INFO);
            statement.setString(1, entity.getStorageFileName());
            statement.setString(2, entity.getOriginalFileName());
            statement.setLong(3, entity.getSize());
            statement.setString(4, entity.getType());
            statement.setString(5, entity.getUrl());
            return statement;
        }, keyHolder);

        entity.setId((Long) keyHolder.getKey());
    }

    @Override
    public void delete(Long aLong) {
        // TODO
    }
}

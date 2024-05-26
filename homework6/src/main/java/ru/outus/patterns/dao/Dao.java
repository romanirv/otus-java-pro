package ru.outus.patterns.dao;

import java.sql.SQLException;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(Long id);
    T save(T t) throws SQLException;
    T update(T t) throws SQLException;
    void delete(Long id) throws SQLException;
}

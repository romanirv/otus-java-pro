package ru.outus.patterns.dao;

import java.sql.SQLException;
import java.util.Optional;

public interface Dao<Entity> {
    Optional<Entity> get(Long id);
    Entity save(Entity t) throws SQLException;
    Entity update(Entity t) throws SQLException;
    void delete(Long id) throws SQLException;
}

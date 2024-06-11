package ru.outus.db.data;

import ru.outus.db.data.exception.DbError;

import java.util.List;
import java.util.Optional;

public interface AbstractRepository<T> {
    T create(T entity) throws DbError;

    void update(T entity) throws DbError;

    void deleteById(Long id) throws DbError;

    Optional<T> findById(Long id) throws DbError;

    List<T> findAll() throws DbError;

    void deleteAll() throws DbError;
}

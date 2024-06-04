package ru.outus.db.data;

import java.util.List;
import java.util.Optional;

public interface AbstractRepository<T> {
    T create(T entity);

    boolean update(T entity);

    boolean deleteById(Long id);

    Optional<T> findById(Long id);

    List<T> findAll();

    boolean deleteAll();
}

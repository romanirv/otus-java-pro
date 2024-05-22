package ru.outus.patterns.dao;

import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(Long id);
    T save(T t);
    T update(T t);
    T delete(T t);
}

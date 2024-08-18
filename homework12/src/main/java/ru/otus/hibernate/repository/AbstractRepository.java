package ru.otus.hibernate.repository;

import java.util.List;
import java.util.Optional;

public interface AbstractRepository<T> {
    void insert(T t);
    boolean deleteById(Long id);
    Optional<T> findById(Long id, boolean isLoadAll);
    List<T> findAll();
}

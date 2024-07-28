package ru.otus.hibernate.repository;

public interface AbstractRepository<T> {

    void insert(T t);
    void deleteById(Long id);
    T findById(Long id);
}

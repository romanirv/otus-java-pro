package ru.outus.db.data.impl;

import ru.outus.db.data.AbstractRepository;
import ru.outus.db.data.DataSource;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class AbstractRepositoryImpl<T> implements AbstractRepository<T> {
    private final DataSource dataSource;
    private final Class<?> cls;
    private final Field[] classFields;

    public AbstractRepositoryImpl(DataSource dataSource, Class<T> cls) {
        this.dataSource = dataSource;
        this.cls = cls;
        this.classFields = cls.getDeclaredFields();
    }

    @Override
    public T create(T entity) {
        return null;
    }

    @Override
    public boolean update(T entity) {
        return false;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        return List.of();
    }

    @Override
    public boolean deleteAll() {
        return false;
    }
}

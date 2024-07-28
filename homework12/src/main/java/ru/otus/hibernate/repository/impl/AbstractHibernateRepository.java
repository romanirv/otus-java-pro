package ru.otus.hibernate.repository.impl;

import org.hibernate.SessionFactory;
import ru.otus.hibernate.repository.AbstractRepository;

public class AbstractHibernateRepository<T> implements AbstractRepository<T> {

    private final SessionFactory sessionFactory;
    public AbstractHibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void insert(T t) {

    }

    public void deleteById(Long id) {

    }

    public T findById(Long id) {
        return null;
    }

}

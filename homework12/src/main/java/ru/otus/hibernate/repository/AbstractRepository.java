package ru.otus.hibernate.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public abstract class AbstractRepository<T> {

    protected final Class<T> cls;

    protected final SessionFactory sessionFactory;

    public T insert(T t) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            T merged = session.merge(t);
            session.getTransaction().commit();

            return merged;
        }
    }

    public boolean deleteById(Long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            boolean result = false;
            /// TODO!:
            T entity = session.find(cls, id);
            if (entity != null) {
                session.remove(entity);
                result = true;
            }

            transaction.commit();
            return result;
        }
    }

    public Optional<T> findByIdLazy(Long id) {
        try (Session session = sessionFactory.getCurrentSession()) {

            Transaction transaction = session.beginTransaction();
            T entity = session.get(cls, id);
            transaction.commit();

            return entity != null ? Optional.of(entity) : Optional.empty();
        }
    }

    public abstract Optional<T> findByIdAndLoad(Long id);

    public List<T> findAllLazy() {
        try (Session session = sessionFactory.getCurrentSession()) {

            Transaction transaction = session.beginTransaction();
            List<T> result = session.createQuery("SELECT a FROM " + cls.getSimpleName() + " a", cls).getResultList();
            transaction.commit();

            return result;
        }
    }
}

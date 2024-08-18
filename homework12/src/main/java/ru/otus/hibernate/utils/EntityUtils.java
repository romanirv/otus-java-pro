package ru.otus.hibernate.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityUtils {

    public static <T> List<T> findAll(SessionFactory sessionFactory, Class<T> cls) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            return session.createQuery("SELECT a FROM " + cls.getSimpleName() + " a", cls).getResultList();
        }
    }

    public static <T> T insert(SessionFactory sessionFactory, T entity) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            T merged = session.merge(entity);
            session.getTransaction().commit();

            return merged;
        }
    }

    public static <T> boolean deleteById(SessionFactory sessionFactory, Class<T> cls, Long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            boolean result = false;
            T entity = session.get(cls, id);
            if (entity != null) {
                session.remove(entity);
                result = true;
            }
            session.getTransaction().commit();
            return result;
        }
    }
}

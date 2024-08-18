package ru.otus.hibernate.repository.impl;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.otus.hibernate.entities.Product;
import ru.otus.hibernate.repository.AbstractRepository;
import ru.otus.hibernate.utils.EntityUtils;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ProductRepository implements AbstractRepository<Product> {

    private final SessionFactory sessionFactory;

    @Override
    public void insert(Product product) {
        EntityUtils.insert(sessionFactory, product);
    }

    @Override
    public boolean deleteById(Long id) {
        return EntityUtils.deleteById(sessionFactory, Product.class, id);
    }

    @Override
    public Optional<Product> findById(Long id, boolean isLoadAll) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Product product = session.get(Product.class, id);
            if (product != null && isLoadAll) {
                Hibernate.initialize(product.getCustomers());
            }
            return product != null ? Optional.of(product) : Optional.empty();
        }
    }

    @Override
    public List<Product> findAll() {
        return EntityUtils.findAll(sessionFactory, Product.class);
    }
}

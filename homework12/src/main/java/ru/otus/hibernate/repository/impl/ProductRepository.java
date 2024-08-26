package ru.otus.hibernate.repository.impl;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.otus.hibernate.entities.Product;
import ru.otus.hibernate.repository.AbstractRepository;

import java.util.Optional;

public class ProductRepository extends AbstractRepository<Product> {

    public ProductRepository(SessionFactory sessionFactory) {
        super(Product.class, sessionFactory);
    }

    @Override
    public Optional<Product> findByIdAndLoad(Long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Product product = session.get(Product.class, id);
            if (product != null) {
                Hibernate.initialize(product.getCustomers());
            }
            return product != null ? Optional.of(product) : Optional.empty();
        }
    }
}

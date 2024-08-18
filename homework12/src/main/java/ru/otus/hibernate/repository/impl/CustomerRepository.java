package ru.otus.hibernate.repository.impl;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.otus.hibernate.entities.Customer;
import ru.otus.hibernate.repository.AbstractRepository;
import ru.otus.hibernate.utils.EntityUtils;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CustomerRepository implements AbstractRepository<Customer> {

    private final SessionFactory sessionFactory;

    @Override
    public void insert(Customer customer) {
        EntityUtils.insert(sessionFactory, customer);
    }

    @Override
    public boolean deleteById(Long id) {
        return EntityUtils.deleteById(sessionFactory, Customer.class, id);
    }

    @Override
    public Optional<Customer> findById(Long id, boolean isLoadAll) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Customer customer = session.get(Customer.class, id);
            if (customer != null && isLoadAll) {
                Hibernate.initialize(customer.getProducts());
            }
            return customer != null ? Optional.of(customer) : Optional.empty();
        }
    }

    @Override
    public List<Customer> findAll() {
        return EntityUtils.findAll(sessionFactory, Customer.class);
    }
}

package ru.otus.hibernate.repository.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.otus.hibernate.entities.Customer;
import ru.otus.hibernate.entities.Product;
import ru.otus.hibernate.repository.AbstractRepository;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

public class CustomerRepository extends AbstractRepository<Customer> {

    private static final Log LOGGER = LogFactory.getLog(MethodHandles.lookup().lookupClass());

    public CustomerRepository(SessionFactory sessionFactory) {
        super(Customer.class, sessionFactory);
    }

    public void addProduct(Long customerId, Long productId) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();

            Customer customer = session.getReference(Customer.class, customerId);
            Product product = session.getReference(Product.class, productId);

            ///!TODO: к сожалению, чтобы добавить новый продукт к существующему покупателю,
            /// получается необходимо загрузить все существующие продукты, а затем добавить новый, проблема N+1,
            /// правильное решение пока не нашел.
            customer.getProducts().add(product);
            customer.getProductsDetail().put(product.getName(), product.getCost());

            session.merge(customer);

            transaction.commit();
        }
    }

    @Override
    public Optional<Customer> findByIdAndLoad(Long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();

            Customer customer = session.find(Customer.class, id);
            if (customer != null) {
                Hibernate.initialize(customer.getProducts());
                Hibernate.initialize(customer.getProductsDetail());
            }

            transaction.commit();
            return customer != null ? Optional.of(customer) : Optional.empty();
        }
    }
}

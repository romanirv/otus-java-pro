package ru.otus.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import ru.otus.hibernate.configurations.JavaBasedSessionFactory;
import ru.otus.hibernate.entities.Customer;
import ru.otus.hibernate.entities.Product;
import ru.otus.hibernate.repository.impl.AbstractHibernateRepository;
import ru.otus.hibernate.services.ShoppingService;
import ru.otus.hibernate.services.impl.ShoppingServiceImpl;

import java.lang.invoke.MethodHandles;

public class Application {
	private static final Log LOGGER = LogFactory.getLog(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) {

		try (SessionFactory sessionFactory = JavaBasedSessionFactory.getSessionFactory(System.getenv())) {

			AbstractHibernateRepository<Product> productRepository
					= new AbstractHibernateRepository<>(sessionFactory);
			AbstractHibernateRepository<Customer> customerRepository
					= new AbstractHibernateRepository<>(sessionFactory);

			dataInitialize(productRepository, customerRepository);

			ShoppingService shoppingService
					= new ShoppingServiceImpl(productRepository, customerRepository);
			shoppingService.start();
		} catch (ExceptionInInitializerError e) {
			LOGGER.error("Initializer error: " + e.getLocalizedMessage());
		}
	}

	public static void dataInitialize(AbstractHibernateRepository<Product> productRepository,
									  AbstractHibernateRepository<Customer> customerRepository) {
//		for (int i = 0; i < 100; ++i) {
//			productRepository.insert(new Product("sdsd", new BigDecimal("100.0"), ));
//		}
	}
}

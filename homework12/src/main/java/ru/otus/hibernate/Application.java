package ru.otus.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import ru.otus.hibernate.configurations.JavaBasedSessionFactory;
import ru.otus.hibernate.entities.Customer;
import ru.otus.hibernate.entities.Product;
import ru.otus.hibernate.repository.AbstractRepository;
import ru.otus.hibernate.repository.impl.CustomerRepository;
import ru.otus.hibernate.repository.impl.ProductRepository;
import ru.otus.hibernate.services.ShoppingService;
import ru.otus.hibernate.services.impl.ShoppingServiceImpl;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Application {
	private static final Log LOGGER = LogFactory.getLog(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) {

		try (SessionFactory sessionFactory = JavaBasedSessionFactory.getSessionFactory(System.getenv())) {

			AbstractRepository<Product> productRepository
					= new ProductRepository(sessionFactory);
			AbstractRepository<Customer> customerRepository
					= new CustomerRepository(sessionFactory);

			dataInitialize(productRepository, customerRepository);

			ShoppingService shoppingService
					= new ShoppingServiceImpl(productRepository, customerRepository);
			shoppingService.start();
		} catch (ExceptionInInitializerError e) {
			LOGGER.error("Initializer error: " + e.getLocalizedMessage());
		} catch (java.io.IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void dataInitialize(AbstractRepository<Product> productRepository,
									  AbstractRepository<Customer> customerRepository) {

//		List<Product> products = createProducts(10);
//		List<Customer> customers = createCustomers(5, products);

		Customer customer = new Customer();
		customer.setName("Customer-0");

		Product product1 = new Product();
		product1.setName("Product-0");
		product1.setCost(new BigDecimal("100.0"));

		Product product2 = new Product();
		product2.setName("Product-1");
		product2.setCost(new BigDecimal("110.0"));

		customer.setProducts(Set.of(product1, product2));
		customerRepository.insert(customer);

		Product product3 = new Product();
		product3.setName("Product-3");
		product3.setCost(new BigDecimal("115.0"));
		productRepository.insert(product3);

//		customers.forEach(customerRepository::insert);
//		products.forEach(productRepository::insert);
	}

	private static List<Product> createProducts(int count) {
		List<Product> products = new ArrayList<>();
		BigDecimal initialCount = new BigDecimal("100.0");
		for (int i = 0; i < count; ++i) {
			Product product = new Product();
			product.setName("Product-" + i);
			product.setCost(initialCount.add(new BigDecimal(i)));
			products.add(product);
		}
		return products;
	}

	private static List<Customer> createCustomers(int count, List<Product> products) {
		List<Customer> customers = new ArrayList<>();

		Customer customer0 = new Customer();
		customer0.setName("Customer-" + 0);
		customer0.setProducts(Set.of(products.get(0), products.get(6)));
		products.get(0).setCustomers(Set.of(customer0));
		products.get(6).setCustomers(Set.of(customer0));
		customers.add(customer0);

		for (int i = 1; i < count; ++i) {
			Customer customer = new Customer();
			customer.setName("Customer-" + i);
			customer.setProducts(Set.of(products.get(i)));
			products.get(i).setCustomers(Set.of(customer));
			customers.add(customer);
		}
		return customers;
	}
}

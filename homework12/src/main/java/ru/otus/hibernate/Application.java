package ru.otus.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import ru.otus.hibernate.configurations.JavaBasedSessionFactory;
import ru.otus.hibernate.entities.Customer;
import ru.otus.hibernate.entities.Product;
import ru.otus.hibernate.repository.impl.CustomerRepository;
import ru.otus.hibernate.repository.impl.ProductRepository;
import ru.otus.hibernate.services.ShoppingService;
import ru.otus.hibernate.services.impl.ShoppingServiceImpl;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Application {
	private static final Log LOGGER = LogFactory.getLog(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) {

		try (SessionFactory sessionFactory = JavaBasedSessionFactory.getSessionFactory(System.getenv())) {

			ProductRepository productRepository
					= new ProductRepository(sessionFactory);
			CustomerRepository customerRepository
					= new CustomerRepository(sessionFactory);

			dataInitialize(productRepository, customerRepository);

			ShoppingService shoppingService
					= new ShoppingServiceImpl(productRepository, customerRepository);
			shoppingService.start();
		} catch (ExceptionInInitializerError e) {
			LOGGER.error("Initializer error: " + e.getLocalizedMessage());
		} catch (java.io.IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void dataInitialize(ProductRepository productRepository,
									   CustomerRepository customerRepository) {
		Customer customer = new Customer();
		customer.setName("Customer-0");

		Product product1 = new Product();
		product1.setName("Product-0");
		product1.setCost(new BigDecimal("100.0"));

		Product product2 = new Product();
		product2.setName("Product-1");
		product2.setCost(new BigDecimal("110.0"));

		customer.setProducts(Set.of(product1, product2));

		Map<String, BigDecimal> productsDetail = new HashMap<>();
		productsDetail.put(product1.getName(), product1.getCost());
		productsDetail.put(product2.getName(), product2.getCost());

		customer.setProductsDetail(productsDetail);
		customer = customerRepository.insert(customer);

		Product product3 = new Product();
		product3.setName("Product-3");
		product3.setCost(new BigDecimal("115.0"));
		product3 = productRepository.insert(product3);

		customerRepository.addProduct(customer.getId(), product3.getId());
	}
}

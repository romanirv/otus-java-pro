package ru.otus.spcontext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.spcontext.config.AppConfig;
import ru.otus.spcontext.entiry.Product;
import ru.otus.spcontext.repository.ProductRepository;
import ru.otus.spcontext.services.CartManager;

import java.io.IOException;
import java.math.BigDecimal;

@ComponentScan
public class Application {

    private static final int initialProductsCount = 10;

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ProductRepository productRepository = context.getBean(ProductRepository.class);
        createRandomProducts(productRepository);

        CartManager cartManager = new CartManager(context);
        cartManager.start();
    }

    public static void createRandomProducts(ProductRepository productRepository) {
        for (int i = 0; i < initialProductsCount; i++) {
            productRepository.save(new Product(i, "Product" + i, new BigDecimal(100 + 1)));
        }
    }
}
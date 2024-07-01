package ru.otus.spcontext.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.otus.spcontext.repository.ProductRepository;
import ru.otus.spcontext.repository.impl.ProductRepositoryImpl;
import ru.otus.spcontext.services.Cart;
import ru.otus.spcontext.services.impl.CartImpl;

@Configuration
public class AppConfig {

    @Bean
    public ProductRepository productRepository() {
        return new ProductRepositoryImpl();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Cart cart(ProductRepository productRepository) {
        return new CartImpl(productRepository);
    }
}

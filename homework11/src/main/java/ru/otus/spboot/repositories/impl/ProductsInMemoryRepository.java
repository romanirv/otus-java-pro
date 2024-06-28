package ru.otus.spboot.repositories.impl;

import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spboot.entities.Product;
import ru.otus.spboot.repositories.ProductsRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
@NoArgsConstructor
public class ProductsInMemoryRepository implements ProductsRepository {

    private final static Logger logger = Logger.getLogger(ProductsInMemoryRepository.class.getName());
    private final List<Product> products = new ArrayList<>();
    private long autoIncrementId = 1;

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Optional<Product> findById(long id) {
        return products.stream().filter(product -> product.getId() == id).findFirst();
    }

    @Override
    public Product save(Product product) {
        product.setId(autoIncrementId++);
        products.add(product);
        return product;
    }

    @PostConstruct
    public void init() {
        logger.info("Initializing repository");
        for (int i = 0; i < 10; i++) {
            products.add(new Product(autoIncrementId, "Product-" + i, new BigDecimal(100 + i)));
            ++autoIncrementId;
        }
    }
}

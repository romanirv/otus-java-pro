package ru.outus.db.services;

import ru.outus.db.data.AbstractRepository;
import ru.outus.db.entiry.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ProductService {

    private final AbstractRepository<Product> productRepository;

    public ProductService(AbstractRepository<Product> productRepository) {
        this.productRepository = productRepository;
    }

    public Product createNewProduct(String title, BigDecimal price) {
        Product product = new Product();
        product.setTitle(title);
        product.setPrice(price);
        return productRepository.create(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public boolean changeProductPrice(Long id, BigDecimal newPrice) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return false;
        }
        product.get().setPrice(newPrice);
        return productRepository.update(product.get());
    }
}

package ru.outus.db.services;

import ru.outus.db.data.AbstractRepository;
import ru.outus.db.data.exception.DbError;
import ru.outus.db.entiry.Product;

import java.util.List;
import java.util.Optional;

public class ProductService {

    private final AbstractRepository<Product> productRepository;

    public ProductService(AbstractRepository<Product> productRepository) {
        this.productRepository = productRepository;
    }

    public Product createNewProduct(String title, String price) throws RuntimeException {
        try {
            Product product = new Product();
            product.setTitle(title);
            product.setPrice(price);
            return productRepository.create(product);
        } catch (DbError e) {
            throw new RuntimeException("Create product error: " + e.getLocalizedMessage());
        }
    }

    public List<Product> getAllProducts() throws RuntimeException {
        try {
            return productRepository.findAll();
        } catch (DbError e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public boolean changeProductPrice(Long id, String newPrice) {
        try {
            Optional<Product> product = productRepository.findById(id);
            if (product.isEmpty()) {
                return false;
            }
            product.get().setPrice(newPrice);
            productRepository.update(product.get());
            return true;
        } catch (DbError e) {
            return false;
        }
    }

    public void deleteProductById(Long id) throws RuntimeException {
        try {
            productRepository.deleteById(id);
        } catch (DbError e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public void deleteAllProducts() throws RuntimeException {
        try {
            productRepository.deleteAll();
        } catch (DbError e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public Optional<Product> getProductById(Long id) throws RuntimeException {
        try {
            return productRepository.findById(id);
        } catch (DbError e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}

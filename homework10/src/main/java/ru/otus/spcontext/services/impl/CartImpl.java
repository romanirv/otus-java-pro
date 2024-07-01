package ru.otus.spcontext.services.impl;

import ru.otus.spcontext.entiry.Product;
import ru.otus.spcontext.repository.ProductRepository;
import ru.otus.spcontext.services.Cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CartImpl implements Cart {

    private final ProductRepository productRepository;
    private final Map<Long, Product> products = new HashMap<>();

    public CartImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean addProduct(long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            return false;
        }
        if (!products.containsKey(productId)) {
            products.put(productId, product.get());
        }
        return true;
    }

    @Override
    public boolean removeProduct(long productId) {
        if (products.containsKey(productId)) {
            products.remove(productId);
            return true;
        }
        return false;
    }

    @Override
    public List<Product> getAllProducts() {
        return products.values().stream().toList();
    }
}

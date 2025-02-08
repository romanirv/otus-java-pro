package ru.otus.spdata.jdbc.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spdata.jdbc.dtos.ProductInfo;
import ru.otus.spdata.jdbc.entities.Product;
import ru.otus.spdata.jdbc.repositories.ProductsRepository;
import ru.otus.spdata.jdbc.services.ProductsService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;

    public ProductsServiceImpl(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }
    @Override
    public Optional<Product> getProductById(Long id) {
        return productsRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productsRepository.findAll();
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductInfo data) {
        Product product = new Product();
        product.setId(id);
        product.setPrice(new BigDecimal(data.getPrice()));
        product.setTitle(data.getTitle());

        return productsRepository.save(product);
    }

    @Override
    @Transactional
    public Product updatePrice(Long id, BigDecimal price) {
        return productsRepository.updatePrice(id, price);
    }

    @Override
    @Transactional
    public Product createProduct(ProductInfo productInfo) {
        Product product = new Product();
        product.setTitle(productInfo.getTitle());
        product.setPrice(new BigDecimal(productInfo.getPrice()));
        return productsRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        productsRepository.deleteById(id);
    }
}

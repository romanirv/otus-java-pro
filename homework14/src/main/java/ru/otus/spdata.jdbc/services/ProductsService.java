package ru.otus.spdata.jdbc.services;

import ru.otus.spdata.jdbc.dtos.ProductInfo;
import ru.otus.spdata.jdbc.entities.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductsService {
    Optional<Product> getProductById(Long id);
    List<Product> getAllProducts();

    Product updateProduct(Long id, ProductInfo data);

    Product updatePrice(Long id, BigDecimal price);
    Product createProduct(ProductInfo productInfo);

    void deleteProduct(Long id);
}

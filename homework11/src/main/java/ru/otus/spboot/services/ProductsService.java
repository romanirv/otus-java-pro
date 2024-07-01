package ru.otus.spboot.services;

import ru.otus.spboot.dtos.CreateProductDto;
import ru.otus.spboot.entities.Product;

import java.util.List;

public interface ProductsService {
    Product getProductById(long id);
    List<Product> getAllProducts();
    Product createProduct(CreateProductDto createProductDto);
}

package ru.otus.spboot.repositories;

import ru.otus.spboot.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository {
    List<Product> findAll();
    Optional<Product> findById(long id);
    Product save(Product product);
}

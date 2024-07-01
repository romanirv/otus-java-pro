package ru.otus.spcontext.repository;

import ru.otus.spcontext.entiry.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    void save(Product product);
    List<Product> findAll();
    Optional<Product> findById(long id);
}

package ru.otus.spcontext.services;

import ru.otus.spcontext.entiry.Product;

import java.util.List;

public interface Cart {
    boolean addProduct(long id);
    boolean removeProduct(long id);
    List<Product> getAllProducts();
}
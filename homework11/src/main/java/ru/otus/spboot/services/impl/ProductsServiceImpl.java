package ru.otus.spboot.services.impl;

import org.springframework.stereotype.Service;
import ru.otus.spboot.dtos.CreateProductDto;
import ru.otus.spboot.entities.Product;
import ru.otus.spboot.repositories.ProductsRepository;
import ru.otus.spboot.services.ProductsService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;

    public ProductsServiceImpl(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public Product getProductById(long id) {
        return productsRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> getAllProducts() {
        return productsRepository.findAll();
    }

    @Override
    public Product createProduct(CreateProductDto createProductDto) {
        return productsRepository.save(new Product(createProductDto.getTitle(),
                new BigDecimal(createProductDto.getPrice())));
    }
}

package ru.otus.spboot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.spboot.dtos.CreateProductDto;
import ru.otus.spboot.entities.Product;
import ru.otus.spboot.services.ProductsService;

import java.util.List;
import java.util.logging.Logger;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductsController {
    private final Logger logger = Logger.getLogger(ProductsController.class.getName());
    private final ProductsService productService;

    @GetMapping("/{id}")
    public Product getProductDetails(@PathVariable Long id) {
        logger.info("Get product details by id: " + id);
        return productService.getProductById(id);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        logger.info("Get all products");
        return productService.getAllProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody CreateProductDto createProductDto) {
        logger.info("Create product: " + createProductDto);
        return productService.createProduct(createProductDto);
    }
}

package ru.otus.spdata.jdbc.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.spdata.jdbc.dtos.ProductInfo;
import ru.otus.spdata.jdbc.dtos.ProductPrice;
import ru.otus.spdata.jdbc.entities.Product;
import ru.otus.spdata.jdbc.services.ProductsService;

import java.util.List;
import java.util.logging.Logger;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductsController {
    private final Logger logger = Logger.getLogger(ProductsController.class.getName());
    private final ProductsService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        logger.info("Get product by id: " + id);
        return ResponseEntity.ok(productService.getProductById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping
    public List<Product> getAllProducts() {
        logger.info("Get all products");
        return productService.getAllProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody ProductInfo productInfo) {
        logger.info("Create new product: " + productInfo);
        return productService.createProduct(productInfo);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductInfo productInfo) {
        logger.info("Update product: id=" + id + " " + productInfo);
        return productService.updateProduct(id, productInfo);
    }


    @PutMapping("/{id}/price")
    public Product updatePrice(@PathVariable Long id, @RequestBody ProductPrice price) {
        logger.info("Update price id=" + id + " price=" + price.getPrice());
        return productService.updatePrice(id, price.getPrice());
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        logger.info("Delete product id: " + id);
        productService.deleteProduct(id);
        return "success";
    }
}

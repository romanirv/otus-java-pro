package ru.otus.spboot.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private long id;
    private String title;
    private BigDecimal price;

    public Product(String title, BigDecimal price) {
        this.title = title;
        this.price = price;
    }
}

package ru.otus.spcontext.entiry;

import java.math.BigDecimal;

public class Product {

    public Product(long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return  "[id: " + id + ", name: " + name + ", price: " + price + "]";
    }

    private long id;
    private String name;
    private BigDecimal price;
}

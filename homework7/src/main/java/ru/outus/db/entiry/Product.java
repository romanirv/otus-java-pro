package ru.outus.db.entiry;

import ru.outus.db.annotations.MyField;
import ru.outus.db.annotations.MyPrimaryKeyField;
import ru.outus.db.annotations.MyTable;

import java.math.BigDecimal;

@MyTable(title = "product")
public class Product {
    @MyField
    @MyPrimaryKeyField
    private Long id;
    @MyField(name = "title")
    private String title;
    @MyField(name = "price")
    private BigDecimal price;

    public Product() {

    }

    public Product(Long id, String title, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}

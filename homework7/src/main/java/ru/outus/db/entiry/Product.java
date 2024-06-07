package ru.outus.db.entiry;

import ru.outus.db.annotations.MyField;
import ru.outus.db.annotations.MyPrimaryKeyField;
import ru.outus.db.annotations.MyTable;

@MyTable(title = "product")
public class Product {
    @MyField
    @MyPrimaryKeyField
    private Long id;
    private String title;
    @MyField(name = "my_price")
    private String price;

    public Product() {
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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

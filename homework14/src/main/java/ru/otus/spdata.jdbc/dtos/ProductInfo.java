package ru.otus.spdata.jdbc.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductInfo {
    private String title;
    private String price;

    @Override
    public String toString() {
        return "title: " + title + ", price: " + price;
    }
}

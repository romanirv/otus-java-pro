package ru.otus.spboot.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateProductDto {
    private String title;
    private String price;

    @Override
    public String toString() {
        return "title: " + title + ", price: " + price;
    }
}

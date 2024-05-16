package ru.outus.patterns.entity;

import java.math.BigDecimal;

public final class Product {
    private final Long id;

    private final String title;

    private final String description;

    private final BigDecimal cost;

    private final Long weight;

    private final Long width;

    private final Long length;

    private final Long height;

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    @Override
    public String toString() {
        return "Product [id: " + id  + ", " +
                "title: " + title + ", " +
                "description: " + description + ", " +
                "cost: " + cost + ", " +
                "weight: " + weight + ", " +
                "width: " + width + ", " +
                "length: " + length + ", " +
                "height: " + height + "]";
    }

    private Product(ProductBuilder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.cost = builder.cost;
        this.weight = builder.weight;
        this.width = builder.width;
        this.length = builder.length;
        this.height = builder.height;
    }
    public static final class ProductBuilder {

        private Long id;

        private String title;

        private String description;

        private BigDecimal cost;

        private Long weight;

        private Long width;

        private Long length;

        private Long height;

        public ProductBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProductBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ProductBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder cost(BigDecimal cost) {
            this.cost = cost;
            return this;
        }

        public ProductBuilder weight(Long weight) {
            this.weight = weight;
            return this;
        }

        public ProductBuilder width(Long width) {
            this.width = width;
            return this;
        }

        public ProductBuilder length(Long length) {
            this.length = length;
            return this;
        }

        public ProductBuilder height(Long height) {
            this.height = height;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

}
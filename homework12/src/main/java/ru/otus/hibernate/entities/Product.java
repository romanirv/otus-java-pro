package ru.otus.hibernate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends AbstractEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "cost")
    private BigDecimal cost;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
//            CascadeType.DETACH,
            CascadeType.MERGE})
//            CascadeType.REFRESH,
//            CascadeType.PERSIST})
    @JoinTable(name = "customers_and_products",
            inverseJoinColumns = @JoinColumn(name = "customer_id", nullable = false),
            joinColumns = @JoinColumn(name = "product_id", nullable = false),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Set<Customer> customers;

    @Override
    public String toString() {
        return "{" + this.getId() + "," + this.getName() + "," + this.getCost() + "}";
    }
}

package ru.otus.hibernate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST})
    @JoinTable(name = "customers_and_products",
            inverseJoinColumns = @JoinColumn(name = "product_id", nullable = false),
            joinColumns = @JoinColumn(name = "customer_id", nullable = false),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Set<Product> products;

    @Override
    public String toString() {
        return "{" + this.getId() + "," + this.getName() + "}";
    }
}

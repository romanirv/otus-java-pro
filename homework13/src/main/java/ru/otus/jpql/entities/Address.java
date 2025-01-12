package ru.otus.jpql.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@AllArgsConstructor
@Getter
@Setter
public class Address {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "street", nullable = false, length = 150)
    private String street;

    @OneToOne(mappedBy = "address")
    private Client client;

    public Address() {
        this.id = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "Address {id: '" + id + "'," +
                "street: '" + street + "'}";
    }
}

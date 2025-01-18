package ru.otus.jpql.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Table(name = "phones")
@Entity
@AllArgsConstructor
@Getter
@Setter
public class Phone {
    @Id
    private UUID id;

    @Column(name = "number", nullable = false, unique = true, length = 20)
    String number;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Phone() {
        this.id = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "Phone {id: '" + id + "', " +
                "number: '" + number + "'}";
    }
}

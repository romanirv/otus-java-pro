package ru.otus.hibernate.entities;

import jakarta.persistence.*;
import lombok.Getter;

@MappedSuperclass
@Getter
public class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
}

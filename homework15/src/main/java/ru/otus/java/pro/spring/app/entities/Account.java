package ru.otus.java.pro.spring.app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "number")
    private String number;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "balance")
    private int balance;

    @Column(name = "is_blocked")
    private boolean isBlocked;
}

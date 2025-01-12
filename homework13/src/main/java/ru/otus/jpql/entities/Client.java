package ru.otus.jpql.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.UUID;

@Table(name = "clients")
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Client {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false, length = 110)
    private String name;

    @OneToMany(mappedBy = "client")
    private Set<Phone> phones;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    public Client() {
        this.id = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "Client {id: '" + id + "', " +
                "name: '" + name + "', " +
                "phones: [" + StringUtils.join(phones, ",") + "]";
    }
}

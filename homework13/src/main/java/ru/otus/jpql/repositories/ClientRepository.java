package ru.otus.jpql.repositories;

import ru.otus.jpql.entities.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {

    void createClient(Client client);

    List<Client> findAll();
    Optional<Client> findById(UUID uuid);

    Optional<Client> findByName(String name);
}

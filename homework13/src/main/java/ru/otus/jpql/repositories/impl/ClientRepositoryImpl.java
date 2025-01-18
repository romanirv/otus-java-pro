package ru.otus.jpql.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import ru.otus.jpql.entities.Client;
import ru.otus.jpql.repositories.ClientRepository;
import ru.otus.jpql.utils.DbConfig;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientRepositoryImpl implements ClientRepository {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory(DbConfig.PERSISTENCE_UNIT_NAME);
    @Override
    public void createClient(Client client) {
        try (EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager()) {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(client);
            entityTransaction.commit();
        }
    }

    @Override
    public List<Client> findAll() {
        try (EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager()) {
            return entityManager
                    .createQuery("SELECT C FROM Client C " +
                            "LEFT JOIN FETCH C.phones " +
                            "LEFT JOIN FETCH C.address", Client.class)
                    .getResultList();
        }
    }

    @Override
    public Optional<Client> findById(UUID id) {
        try (EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager()) {
            Client client = entityManager
                    .createQuery("SELECT C FROM Client C LEFT JOIN FETCH C.phones " +
                                    "LEFT JOIN FETCH C.address WHERE C.id = :id",
                            Client.class)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.of(client);
        }
    }

    public Optional<Client> findByName(String name) {
        try (EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager()) {
            Client client = entityManager
                    .createQuery("SELECT C FROM Client C LEFT JOIN FETCH C.phones LEFT JOIN FETCH C.address WHERE C.name = :name", Client.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(client);
        }
    }
}

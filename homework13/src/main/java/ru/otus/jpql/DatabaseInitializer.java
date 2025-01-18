package ru.otus.jpql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jpql.exceptions.DbConfigError;
import ru.otus.jpql.utils.DbConfig;
import ru.otus.jpql.utils.LiquibaseRunner;

import java.util.List;


@NoArgsConstructor
public class DatabaseInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final String ENTITY_PACKAGE = "ru.otus.jpql.entities.";


    public static void main(String[] args) {
        try {
            LiquibaseRunner.runMigrations();
        } catch (DbConfigError e) {
            LOGGER.error("DB configuration error: " + e.getLocalizedMessage());
            return;
        }

        LOGGER.info("LiquibaseRunner done.");

        try (EntityManagerFactory entityManagerFactory
                     = Persistence.createEntityManagerFactory(DbConfig.PERSISTENCE_UNIT_NAME);
             EntityManager entityManager
                     = entityManagerFactory.createEntityManager()) {

            entityManager.getTransaction().begin();

            LOGGER.info("Rows deleted from all tables table.");

            logTableContents(entityManager, "Phone");
            logTableContents(entityManager, "Address");

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            LOGGER.error("An error occurred while loading the entity class.", e);
        }
    }

    private static void logTableContents(EntityManager entityManager, String entityClassName) {
        try {
            Class<?> entityClass = Class.forName(ENTITY_PACKAGE + entityClassName);

            List<?> resultList = entityManager.createQuery("SELECT e FROM " + entityClassName + " e", entityClass).getResultList();

            LOGGER.info("Table '{}' contains {} records:", entityClassName, resultList.size());

            for (Object entity : resultList) {
                LOGGER.info("{}: {}", entityClassName, entity);
            }
        } catch (ClassNotFoundException e) {
            LOGGER.error("Entity class not found: {}", entityClassName, e);
        }
    }
}

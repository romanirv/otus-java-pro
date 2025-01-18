package ru.otus.jpql.utils;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jpql.exceptions.DbConfigError;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class LiquibaseRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiquibaseRunner.class);

    private static final String DB_CONNECTION_URL = "DB_CONNECTION_URL";
    private static final String DB_USERNAME = "DB_USERNAME";
    private static final String DB_PASSWORD = "DB_PASSWORD";
    private LiquibaseRunner() {
    }

    public static void runMigrations() throws DbConfigError {
        LOGGER.info("LiquibaseRunner run...");

        Map<String, String> properties = System.getenv();

        if (!properties.containsKey(DB_CONNECTION_URL)) {
            LOGGER.error("'" + DB_CONNECTION_URL + "' in config not found!");
            throw new DbConfigError("Required '" + DB_CONNECTION_URL + "'");
        }
        String jdbcUrl = properties.get(DB_CONNECTION_URL); // "jdbc:postgresql://localhost:5432/otus-db";
        if (jdbcUrl == null || jdbcUrl.isBlank()){
            LOGGER.error(DB_CONNECTION_URL + " is null or not valid");
            throw new DbConfigError("Not valid '" + DB_CONNECTION_URL + "'");
        }

        if (!properties.containsKey(DB_USERNAME)) {
            LOGGER.error("'" + DB_USERNAME + "' in config not found!");
            throw new DbConfigError("Required '" + DB_USERNAME + "'");
        }
        String dbUsername = properties.get(DB_USERNAME);
        if (dbUsername == null || dbUsername.isBlank()) {
            LOGGER.error(DB_USERNAME + " is null or not valid");
            throw new DbConfigError("Not valid '" + DB_USERNAME + "'");
        }

        if (!properties.containsKey(DB_PASSWORD)) {
            LOGGER.error("'" + DB_PASSWORD + "' in config not found!");
            throw new DbConfigError("Required '" + DB_PASSWORD + "'");
        }
        String dbPassword = properties.get(DB_PASSWORD);
        if (dbPassword == null || dbPassword.isBlank()) {
            LOGGER.error(DB_PASSWORD + " is not valid");
            throw new DbConfigError("Not valid '" + DB_PASSWORD + "'");
        }

        String changelogFile = "db/changelog/db.changelog-master.yaml";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                    new liquibase.database.jvm.JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changelogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update("");
            LOGGER.info("Liquibase migrations applied successfully.");
        } catch (SQLException | LiquibaseException e) {
            LOGGER.error("Error while running Liquibase migrations", e);
            throw new DbConfigError("Error while running Liquibase migrations: " + e.getLocalizedMessage());
        }
    }
}

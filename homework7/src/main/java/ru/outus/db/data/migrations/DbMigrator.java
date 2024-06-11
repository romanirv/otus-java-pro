package ru.outus.db.data.migrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.outus.db.Migrations;
import ru.outus.db.annotations.MyField;
import ru.outus.db.annotations.MyPrimaryKeyField;
import ru.outus.db.annotations.MyTable;
import ru.outus.db.data.DataSource;
import ru.outus.db.data.exception.DbError;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


public class DbMigrator {
    private static final Logger logger = LoggerFactory.getLogger(DbMigrator.class);
    private static final Map<String, String> dbTypesMapTable = new HashMap<>();

    static {
        dbTypesMapTable.put(String.class.toString(), "varchar(450)");
        dbTypesMapTable.put(Long.class.toString(), "serial");
        dbTypesMapTable.put(BigDecimal.class.toString(), "decimal");
    }


    public static boolean generateDbInitScript(String scriptFilename, List<Class<?>> entityClasses) {
        return DbMigrator.writeScript(scriptFilename, getDbInitScript(entityClasses));
    }

    public static void initializeDb(String initScriptFilename, DataSource dataSource) throws DbError {
        List<String> queries = DbMigrator.readScript(initScriptFilename);

        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try (Statement statement = connection.createStatement()) {
                connection.setAutoCommit(false);

                for (String query : queries) {
                    statement.addBatch(query);
                }
                statement.executeBatch();

                connection.commit();
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DbError(ex.getLocalizedMessage());
                }
            }
            throw new DbError(e.getLocalizedMessage());
        }
    }

    private static String getDbInitScript(List<Class<?>> entityClasses) throws RuntimeException {
        StringBuilder query = new StringBuilder();
        for (Class<?> cls : entityClasses) {
            query.append("CREATE TABLE IF NOT EXISTS ");
            query.append(cls.getAnnotation(MyTable.class).title()).append(" (");

            List<Field> fields = Arrays.stream(cls.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(MyField.class))
                    .toList();
            for (Field f : fields) {
                f.setAccessible(true);
                String name = f.getAnnotation(MyField.class).name();
                if (name.isEmpty()) {
                    name = f.getName();
                }
                query.append(name).append(" ");
                if (!dbTypesMapTable.containsKey(f.getType().toString())) {
                    throw new RuntimeException("Convert filed to db type error!");
                }
                query.append(dbTypesMapTable.get(f.getType().toString()));
                if (f.isAnnotationPresent(MyPrimaryKeyField.class)) {
                    query.append(" ");
                    query.append("not null primary key");
                }
                query.append(", ");
            }
            query.setLength(query.length() - 2);
            query.append(");\n");
        }

        return query.toString();
    }

    private DbMigrator() {

    }

    private static boolean writeScript(String scriptFilename, String script) {
        try (FileWriter stream = new FileWriter(scriptFilename)) {
            stream.write(script);
            logger.info("Script written to {}", scriptFilename);
            return true;
        } catch (IOException e) {
            logger.error("Error writing script to file {} {}", scriptFilename, e.getLocalizedMessage());
            return false;
        }
    }

    private static List<String> readScript(String scriptFilename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(scriptFilename))) {
            List<String> queries = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                queries.add(line);
            }
            return queries;
        } catch (IOException e) {
            throw new RuntimeException("Error reading script from file " + scriptFilename, e);
        }
    }
}

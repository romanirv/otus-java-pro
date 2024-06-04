package ru.outus.db.migrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.outus.db.Migrations;
import ru.outus.db.annotations.MyField;
import ru.outus.db.annotations.MyPrimaryKeyField;
import ru.outus.db.annotations.MyTable;
import ru.outus.db.data.DataSource;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DbMigrator {
    private static final Logger logger = LoggerFactory.getLogger(Migrations.class);

    private static final Map<String, String> dbTypesMapTable = new HashMap<>() {{
            put(String.class.toString(), "varchar(450)");
            put(Long.class.toString(), "bigint");
            put(BigDecimal.class.toString(), "decimal");
    }};

    public static boolean generateDbInitScript(String scriptFilename, List<Class<?>> entityClasses) {
        return DbMigrator.writeScript(scriptFilename, getDbInitScript(entityClasses));
    }

    public static boolean initializeDb(String initScriptFilename, DataSource dataSource) throws SQLException {
        List<String> queries = DbMigrator.readScript(initScriptFilename);
        Connection connection = dataSource.getConnection();

        try {
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            for (String query : queries) {
                statement.execute(query);
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            return false;
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
            for (Field f : fields) { // TODO Сделать использование геттеров
                f.setAccessible(true);
                query.append(f.getName()).append(" ");
                if (!dbTypesMapTable.containsKey(f.getType().toString())) {
                    throw new RuntimeException("Convert filed to db type error!");
                }

                query.append(dbTypesMapTable.get(f.getType().toString()));
                query.append(" NOT NULL, ");
            }

            List<Field> primaryKeysFiled = fields.stream()
                    .filter(f -> f.isAnnotationPresent(MyPrimaryKeyField.class))
                    .toList();
            if (primaryKeysFiled.size() > 1) {
                throw new RuntimeException("Multiple primary key fields found!");
            }

            if (primaryKeysFiled.size() == 1) {
                Field primaryKeyFiled = primaryKeysFiled.get(0);
                primaryKeyFiled.setAccessible(true);
                query.append("PRIMARY KEY (").append(primaryKeyFiled.getName()).append(")");
            } else {
                query.setLength(query.length() - 2);
            }
            query.append(");\n");
        }

        return query.toString();
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

package ru.outus.db.data.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.outus.db.annotations.MyField;
import ru.outus.db.annotations.MyPrimaryKeyField;
import ru.outus.db.annotations.MyTable;
import ru.outus.db.data.AbstractRepository;
import ru.outus.db.data.DataSource;
import ru.outus.db.data.exception.DbError;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class AbstractRepositoryImpl<T> implements AbstractRepository<T> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRepositoryImpl.class);

    private final Class<T> cls;
    private final List<Field> fieldsWithoutPrimaryKeys;
    private final Field fieldPrimaryKey;

    private final PreparedStatement insertPreparedStatement;
    private final PreparedStatement updatePreparedStatement;
    private final PreparedStatement deleteByIdPreparedStatement;
    private final PreparedStatement findByIdPreparedStatement;
    private final PreparedStatement findAllPreparedStatement;
    private final PreparedStatement deleteAllPreparedStatement;

    public AbstractRepositoryImpl(DataSource dataSource, Class<T> cls) throws DbError {
        this.cls = cls;
        this.fieldsWithoutPrimaryKeys = this.getFieldsWithoutPrimaryKeys(cls);
        this.fieldPrimaryKey = this.getFieldPrimaryKey(cls);

        String tableTitle = this.getTableTitle(cls);
        String primaryKeyName = this.getFiledName(this.fieldPrimaryKey);
        List<String> columnsNamesWithoutPrimaryKey = this.getColumnNamesWithoutPrimaryKey(this.fieldsWithoutPrimaryKeys);

        this.insertPreparedStatement = this.buildInsertPreparedStatement(
                dataSource, tableTitle, primaryKeyName, columnsNamesWithoutPrimaryKey);
        this.updatePreparedStatement = this.buildUpdatePreparedStatement(
                dataSource, tableTitle, primaryKeyName, columnsNamesWithoutPrimaryKey);
        this.findByIdPreparedStatement = this.buildFindByIdPreparedStatement(
                dataSource, tableTitle, primaryKeyName, columnsNamesWithoutPrimaryKey);
        this.findAllPreparedStatement = this.buildFindAllPreparedStatement(
                dataSource, tableTitle, primaryKeyName, columnsNamesWithoutPrimaryKey);
        this.deleteByIdPreparedStatement = this.buildDeleteByIdPreparedStatement(
                dataSource, tableTitle, primaryKeyName);
        this.deleteAllPreparedStatement = this.buildDeleteAllPreparedStatement(
                dataSource, tableTitle);
    }

    @Override
    public T create(T entity) throws DbError {
        try {
            int i = 1;
            for (Field field : fieldsWithoutPrimaryKeys) {
                field.setAccessible(true);
                insertPreparedStatement.setObject(i, field.get(entity));
                ++i;
            }

            logger.info("Execute query: {}", insertPreparedStatement);
            int index = insertPreparedStatement.executeUpdate();
            try (ResultSet rs = insertPreparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    fieldPrimaryKey.setAccessible(true);
                    fieldPrimaryKey.set(entity, rs.getLong(index));
                    return entity;
                }
            }
        } catch (Exception e) {
            throw new DbError(e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public void update(T entity) throws DbError {
        try {
            int i = 1;
            for (Field field : fieldsWithoutPrimaryKeys) {
                field.setAccessible(true);
                updatePreparedStatement.setObject(i, field.get(entity));
                ++i;
            }
            updatePreparedStatement.setObject(i, fieldPrimaryKey.get(entity));
            logger.info("Execute query: {}", updatePreparedStatement);
            updatePreparedStatement.executeUpdate();
        } catch (IllegalAccessException | SQLException e) {
            throw new DbError(e.getLocalizedMessage());
        }
    }

    @Override
    public void deleteById(Long id) throws DbError {
        try {
            deleteByIdPreparedStatement.setObject(1, id);
            logger.info("Execute query: {}", deleteByIdPreparedStatement);
            deleteByIdPreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbError(e.getLocalizedMessage());
        }
    }

    @Override
    public Optional<T> findById(Long id) throws DbError{
        try {
            findByIdPreparedStatement.setObject(1, id);
            logger.info("Execute query: {}", findByIdPreparedStatement);
            final ResultSet rs = findByIdPreparedStatement.executeQuery();
            if (rs.next()) {
                T result = createNewObject(cls);
                fieldPrimaryKey.set(result, rs.getLong(1));
                int i = 2;
                for (Field field : fieldsWithoutPrimaryKeys) {
                    field.set(result, rs.getObject(i));
                    ++i;
                }
                return Optional.of(result);
            }
        } catch (IllegalAccessException | SQLException e) {
            throw new DbError(e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() throws DbError {
        List <T> resultList = new ArrayList<>();
        logger.info("Execute query: {}", findAllPreparedStatement);
        try (final ResultSet rs = findAllPreparedStatement.executeQuery()) {
            while (rs.next()) {
                resultList.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DbError(e.getLocalizedMessage());
        }
        return resultList;
    }

    @Override
    public void deleteAll() throws DbError {
        try {
            logger.info("Execute query: {}", deleteAllPreparedStatement);
            deleteAllPreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbError(e.getLocalizedMessage());
        }
    }

    private String getFiledName(Field field) {
        String name = field.getAnnotation(MyField.class).name();
        if (name.isEmpty()) {
            name = field.getName();
        }
        return name;
    }

    private String getTableTitle(Class<T> cls) throws DbError {
        if (!cls.isAnnotationPresent(MyTable.class)) {
            throw new DbError("Class " + cls + " is not entity (no annotation MyTable) ");
        }
        return cls.getAnnotation(MyTable.class).title();
    }

    private List<String> getColumnNamesWithoutPrimaryKey(List<Field> fields) {
        List<String> columnNames = new ArrayList<>();
        fields.forEach(f -> columnNames.add(getFiledName(f)));
        return columnNames;
    }

    private T map(ResultSet rs) throws DbError {
        try {
            T result = createNewObject(cls);
            fieldPrimaryKey.set(result, rs.getLong(1));
            for (int i = 0; i < fieldsWithoutPrimaryKeys.size(); i++) {
                fieldsWithoutPrimaryKeys.get(i).set(result, rs.getObject(i + 2));
            }
            return result;
        } catch (IllegalAccessException | SQLException e) {
            throw new DbError(e.getLocalizedMessage());
        }
    }

    private List<Field> getFieldsWithoutPrimaryKeys(Class<T> cls) throws DbError {
        List<Field> result = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(MyField.class))
                .filter(f -> !f.isAnnotationPresent(MyPrimaryKeyField.class))
                .toList();
        if (result.isEmpty()) {
            throw new DbError("In entity " + cls + " declared (with annotation @MyField) columns not found!");
        }

        return result;
    }

    private Field getFieldPrimaryKey(Class<T> cls) throws DbError {
        List<Field> primaryKeyFields = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(MyPrimaryKeyField.class))
                .toList();

        if (primaryKeyFields.isEmpty()) {
            throw new DbError("In entity " + cls +
                    " declared primary key (with annotation @MyPrimaryKey) not found!");
        }

        if (primaryKeyFields.size() > 1) {
            throw new DbError("In entity " +  cls + " multiple declared primary key!");
        }

        return primaryKeyFields.get(0);
    }

    private PreparedStatement buildInsertPreparedStatement(DataSource dataSource,
                                                           String tableTitle, String primaryKeyName,
                                                           List<String> columnsNamesWithoutPrimaryKey)
            throws DbError {

        StringBuilder query = new StringBuilder("insert into ");

        query.append(tableTitle).append(" (");
        query.append(primaryKeyName).append(", ");

        columnsNamesWithoutPrimaryKey.forEach(c -> query.append(c).append(", "));
        query.setLength(query.length() - 2);

        query.append(") values (default, ");
        columnsNamesWithoutPrimaryKey.forEach(c -> query.append("?, "));
        query.setLength(query.length() - 2);

        query.append(")");
        try {
            return dataSource.getConnection().prepareStatement(query.toString(), new String[] {
                    fieldPrimaryKey.getName() });
        } catch (SQLException e) {
            throw new DbError(e.getLocalizedMessage());
        }
    }

    private PreparedStatement buildUpdatePreparedStatement(DataSource dataSource,
                                                           String tableTitle, String primaryKeyName,
                                                           List<String> columnsNamesWithoutPrimaryKey)
            throws DbError {

        StringBuilder query = new StringBuilder("update ");
        query.append(tableTitle);

        query.append(" set ");
        columnsNamesWithoutPrimaryKey.forEach(c -> query.append(c).append(" = ?, "));
        query.setLength(query.length() - 2);

        query.append(" where ").append(primaryKeyName).append(" = ?");
        try {
            return dataSource.getConnection().prepareStatement(query.toString());
        } catch (SQLException e) {
            throw new DbError(e.getLocalizedMessage());
        }
    }

    private String getFindAllQuery(String tableTitle, String primaryKeyName,
                                   List<String> columnsNamesWithoutPrimaryKey) {
        StringBuilder query = new StringBuilder("select ");

        query.append(primaryKeyName).append(", ");
        columnsNamesWithoutPrimaryKey.forEach(c -> query.append(c).append(", "));
        query.setLength(query.length() - 2);

        query.append(" from ").append(tableTitle);
        return query.toString();
    }

    private PreparedStatement buildFindAllPreparedStatement(DataSource dataSource,
                                                            String tableTitle, String primaryKeyName,
                                                            List<String> columnsNamesWithoutPrimaryKey
    ) throws DbError {
        try {
            return dataSource.getConnection().prepareStatement(
                    getFindAllQuery(tableTitle, primaryKeyName,
                            columnsNamesWithoutPrimaryKey));
        } catch (SQLException e) {
            throw new DbError(e.getLocalizedMessage());
        }
    }

    private PreparedStatement buildFindByIdPreparedStatement(DataSource dataSource,
                                                             String tableTitle, String primaryKeyName,
                                                             List<String> columnsNamesWithoutPrimaryKey)
            throws DbError {

        StringBuilder query = new StringBuilder(
                getFindAllQuery(tableTitle, primaryKeyName,
                        columnsNamesWithoutPrimaryKey));
        query.append(" where ").append(primaryKeyName).append(" = ?");
        try {
            return dataSource.getConnection().prepareStatement(query.toString());
        } catch (SQLException e) {
            throw new DbError(e.getLocalizedMessage());
        }
    }

    private String getDeleteAllQuery(String tableTitle) {
        return "delete from " + tableTitle;
    }

    private PreparedStatement buildDeleteByIdPreparedStatement(DataSource dataSource,
                                                               String tableTitle, String primaryKeyName)
            throws DbError {
        StringBuilder query = new StringBuilder(getDeleteAllQuery(tableTitle));
        query.append(" where ").append(primaryKeyName).append(" = ?");
        try {
            return dataSource.getConnection().prepareStatement(query.toString());
        } catch (SQLException e) {
            throw new DbError(e.getLocalizedMessage());
        }
    }

    private PreparedStatement buildDeleteAllPreparedStatement(DataSource dataSource, String tableTitle)
            throws DbError {
        try {
            return dataSource.getConnection().prepareStatement(getDeleteAllQuery(tableTitle));
        } catch (SQLException e) {
            throw new DbError(e.getLocalizedMessage());
        }
    }


    private T createNewObject(Class<T> cls) throws DbError {
        try {
            Constructor<T> constructor = cls.getConstructor();
            return constructor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException
                 | InstantiationException | IllegalAccessException e) {
            throw new DbError("Instance new object for type " + cls + " error" + e.getLocalizedMessage());
        }
    }
}

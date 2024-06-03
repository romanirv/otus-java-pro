package ru.outus.patterns.dao.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.outus.patterns.dao.Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class DaoTransactionProxy<T> implements Dao<T> {
    private static final Logger logger = LoggerFactory.getLogger(DaoTransactionProxy.class);

    private final Dao<T> realObject;
    private final Connection dbConnection;

    public DaoTransactionProxy(Dao<T> realObject, Connection dbConnections) {
        this.realObject = realObject;
        this.dbConnection = dbConnections;
    }

    @Override
    public Optional<T> get(Long id) {
        return this.realObject.get(id);
    }

    @Override
    public T save(T entity) throws SQLException {
        createTransaction();
        try {
            T result = this.realObject.save(entity);
            commitTransaction();
            return result;
        } catch (SQLException e) {
            rollbackTransaction();
            throw e;
        }
    }

    @Override
    public T update(T entity) throws SQLException {
        createTransaction();
        try {
            T result = this.realObject.update(entity);
            commitTransaction();
            return result;
        } catch (SQLException e) {
            rollbackTransaction();
            throw e;
        }
    }

    @Override
    public void delete(Long entity) throws SQLException {
        createTransaction();
        try {
            realObject.delete(entity);
            commitTransaction();
        } catch (SQLException e){
            rollbackTransaction();
            throw e;
        }
    }

    private void createTransaction() throws SQLException {
        dbConnection.setAutoCommit(false);
        logger.info("Create transaction");
    }

    private void commitTransaction() throws SQLException {
        this.dbConnection.commit();
        logger.info("Commit transaction");
    }

    private void rollbackTransaction() throws SQLException {
        this.dbConnection.rollback();
        logger.info("Rollback transaction");
    }
}

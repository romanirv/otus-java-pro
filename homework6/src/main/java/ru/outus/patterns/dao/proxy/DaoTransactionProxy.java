package ru.outus.patterns.dao.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.outus.patterns.dao.Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Optional;

public class DaoTransactionProxy<Entity> implements Dao<Entity> {
    private static final Logger logger = LoggerFactory.getLogger(DaoTransactionProxy.class);

    private final Dao<Entity> realObject;
    private final Connection dbConnection;

    public DaoTransactionProxy(Dao<Entity> realObject, Connection dbConnections) {
        this.realObject = realObject;
        this.dbConnection = dbConnections;
    }

    @Override
    public Optional<Entity> get(Long id) {
        return this.realObject.get(id);
    }

    @Override
    public Entity save(Entity entity) throws SQLException {
        Savepoint savePoint = createTransaction();
        try {
            Entity result = this.realObject.save(entity);
            commitTransaction();
            return result;
        } catch (SQLException e) {
            rollbackTransaction(savePoint);
            throw e;
        }
    }

    @Override
    public Entity update(Entity entity) throws SQLException {
        Savepoint savePoint = createTransaction();
        try {
            Entity result = this.realObject.update(entity);
            commitTransaction();
            return result;
        } catch (SQLException e) {
            rollbackTransaction(savePoint);
            throw e;
        }
    }

    @Override
    public void delete(Long entity) throws SQLException {
        Savepoint savePoint = createTransaction();
        try {
            realObject.delete(entity);
            commitTransaction();
        } catch (SQLException e){
            rollbackTransaction(savePoint);
            throw e;
        }
    }

    private Savepoint createTransaction() throws SQLException {
        dbConnection.setAutoCommit(false);
        Savepoint result = dbConnection.setSavepoint();
        logger.info("Create transaction");
        return result;
    }

    private void commitTransaction() throws SQLException {
        this.dbConnection.commit();
        logger.info("Commit transaction");
    }

    private void rollbackTransaction(Savepoint savepoint) throws SQLException {
        this.dbConnection.rollback(savepoint);
        logger.info("Rollback transaction");
    }
}

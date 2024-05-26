package ru.outus.patterns.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.outus.patterns.dao.Dao;
import ru.outus.patterns.entity.Account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AccountDao implements Dao<Account> {
    private static final Logger logger = LoggerFactory.getLogger(AccountDao.class);

    private final Connection dbConnection;
    private final PreparedStatement getGetQueryPreparedStmt;
    private final PreparedStatement insertQueryPreparedStmt;
    private final PreparedStatement updateQueryPreparedStmt;
    private final PreparedStatement deleteQueryPreparedStmt;


    public AccountDao(Connection dbConnection) throws SQLException {
        this.dbConnection = dbConnection;
        this.createTable();
        this.getGetQueryPreparedStmt = this.dbConnection.prepareStatement(SQLAccount.GET.query);
        this.insertQueryPreparedStmt = this.dbConnection.prepareStatement(SQLAccount.INSERT.query, new String[] { "id" });
        this.updateQueryPreparedStmt = this.dbConnection.prepareStatement(SQLAccount.UPDATE.query);
        this.deleteQueryPreparedStmt = this.dbConnection.prepareStatement(SQLAccount.DELETE.query);
    }

    @Override
    public Optional<Account> get(Long id) {
        try {
            getGetQueryPreparedStmt.setLong(1, id);
            logger.info("Execute SQL={}", getGetQueryPreparedStmt);
            final ResultSet rs = getGetQueryPreparedStmt.executeQuery();
            if (rs.next()) {
                final Account result = new Account();
                result.setId(Long.parseLong(rs.getString("id")));
                result.setAmount(new BigDecimal(rs.getString("amount")));
                result.setNumber(rs.getString("number"));
                return Optional.of(result);
            }
        } catch (SQLException e) {
            logger.error("Get account by id={} exception: {}", id, e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    @Override
    public Account save(Account account) throws SQLException {
        insertQueryPreparedStmt.setString(1, account.getAmount().toString());
        insertQueryPreparedStmt.setString(2, account.getNumber());
        logger.info("Execute SQL={}", insertQueryPreparedStmt);
        int index = insertQueryPreparedStmt.executeUpdate();
        try (ResultSet rs = insertQueryPreparedStmt.getGeneratedKeys()) {
            if (rs.next()) {
                account.setId(rs.getLong(index));
            }
        }
        return account;
    }

    @Override
    public Account update(Account account) throws SQLException {
        updateQueryPreparedStmt.setString(1, account.getAmount().toString());
        updateQueryPreparedStmt.setString(2, account.getNumber());
        updateQueryPreparedStmt.setLong(3, account.getId());
        logger.info("Execute SQL={}", updateQueryPreparedStmt);
        updateQueryPreparedStmt.executeUpdate();
        return account;
    }

    @Override
    public void delete(Long id) throws SQLException {
        deleteQueryPreparedStmt.setLong(1, id);
        deleteQueryPreparedStmt.executeUpdate();
        logger.info("Execute SQL={}", deleteQueryPreparedStmt);
        logger.info("Delete account id={}", id);
    }

    private void createTable() throws SQLException {
        Statement createQueryStatement = this.dbConnection.createStatement();
        createQueryStatement.execute(SQLAccount.CREATE.query);
    }

    enum SQLAccount {
        CREATE("CREATE TABLE IF NOT EXISTS Accounts"
                + "(id MEDIUMINT not null AUTO_INCREMENT,"
                + " amount       VARCHAR(20),"
                + " number       VARCHAR(50))"),
        GET("SELECT id, amount, number FROM Accounts WHERE Accounts.id = (?)"),
        INSERT("INSERT INTO Accounts (id, amount, number) VALUES (DEFAULT, (?), (?))"),
        DELETE("DELETE FROM Accounts WHERE id = (?)"),
        UPDATE("UPDATE Accounts SET amount = (?), number = (?) WHERE id = (?)");

        final String query;

        SQLAccount(String query) {
            this.query = query;
        }
    }
}

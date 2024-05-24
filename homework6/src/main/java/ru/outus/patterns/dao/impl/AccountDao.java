package ru.outus.patterns.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.outus.patterns.dao.Dao;
import ru.outus.patterns.entity.Account;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

public class AccountDao implements Dao<Account> {
    private static final Logger logger = LoggerFactory.getLogger(AccountDao.class);

    private final Connection dbConnection;

    public AccountDao(Connection dbConnection) throws SQLException {
        this.dbConnection = dbConnection;
        createTable();
    }

    @Override
    public Optional<Account> get(Long id) {
        try (PreparedStatement statement = this.dbConnection.prepareStatement(SQLAccount.GET.query)) {
            statement.setLong(1, id);
            logger.info("Execute SQL=" + statement);
            final ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                final Account result = new Account();
                result.setId(Long.parseLong(rs.getString("id")));
                result.setAmount(new BigDecimal(rs.getString("amount")));
                result.setNumber(rs.getString("number"));
                return Optional.of(result);
            }
        } catch (SQLException e) {
            logger.error("Get account by id=" + id + " exception: " + e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    @Override
    public Account save(Account account) throws SQLException {
        String[] returnId = { "id" };
        try (PreparedStatement statement = this.dbConnection.prepareStatement(SQLAccount.INSERT.query, returnId)) {
            statement.setString(1, account.getAmount().toString());
            statement.setString(2, account.getNumber());
            logger.info("Execute SQL=" + statement);
            int index = statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    account.setId(rs.getLong(index));
                }
            }
        }
        return account;
    }

    @Override
    public Account update(Account account) throws SQLException {
        try (PreparedStatement statement = this.dbConnection.prepareStatement(SQLAccount.UPDATE.query)) {
            statement.setString(1, account.getAmount().toString());
            statement.setString(2, account.getNumber());
            statement.setLong(3, account.getId());
            logger.info("Execute SQL=" + statement);
            statement.executeUpdate();
        }
        return account;

    }

    @Override
    public void delete(Long id) throws SQLException {
        try (PreparedStatement statement = this.dbConnection.prepareStatement(SQLAccount.DELETE.query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            logger.info("Execute SQL=" + statement);
            logger.info("Delete account id=" + id);
        }
    }

    private void createTable() throws SQLException {
        Statement stmt = this.dbConnection.createStatement();
        stmt.execute(SQLAccount.CREATE.query);
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

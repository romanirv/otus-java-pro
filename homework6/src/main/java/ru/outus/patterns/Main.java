package ru.outus.patterns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.outus.patterns.dao.Dao;
import ru.outus.patterns.dao.impl.AccountDao;
import ru.outus.patterns.dao.proxy.DaoTransactionProxy;
import ru.outus.patterns.entity.Account;
import ru.outus.patterns.services.AccountService;
import ru.outus.patterns.services.exceptions.AccountException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static Optional<Connection> createH2InMemoryDbConnection(String dbName) {
        try {
            return Optional.of(DriverManager.getConnection("jdbc:h2:mem:" + dbName));
        } catch (SQLException e) {
            logger.error("Create H2 in memory DB connection: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    public static Optional<Dao<Account>> createAccountDao(Connection dbConnection) {
        try {
            return Optional.of(new DaoTransactionProxy<>(new AccountDao(dbConnection), dbConnection));
        } catch (SQLException e) {
            logger.error("Create account DAO error: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    public static void main(String[] args) {
        String dbName = "TestDB";

        Optional<Connection> connection = createH2InMemoryDbConnection(dbName);
        if (connection.isEmpty()) {
            logger.error("Create DB connection error!");
            return;
        }
        logger.info("Create DB connection success!");

        Optional<Dao<Account>> accountDao = createAccountDao(connection.get());
        if (accountDao.isEmpty()) {
            logger.error("Create account DAO error!");
            return;
        }
        logger.info("Create account DAO success!");

        try {
            AccountService accountService = new AccountService(accountDao.get());

            Account account = accountService.addAccount("18293023", new BigDecimal("100.0"));
            logger.info("Create new account:" +
                    " id=" + account.getId() +
                    " number=" + account.getNumber() +
                    " initialAmount=" + account.getAmount() + " success!");

            Account newAccount = accountService.addAmount(account.getId(), new BigDecimal("10.0"));
            logger.info("Update for account id=" + newAccount.getId() + " amount=" + newAccount.getAmount() + " success!");

            accountService.deleteAccount(newAccount.getId());
            logger.info("Delete account=" + account.getId() + " success!");
        } catch (AccountException e) {
            logger.error("Account exception: " + e.getLocalizedMessage());
        }
    }
}

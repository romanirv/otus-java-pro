package ru.outus.patterns.services;

import ru.outus.patterns.dao.Dao;
import ru.outus.patterns.entity.Account;
import ru.outus.patterns.services.exceptions.AccountException;

import java.math.BigDecimal;
import java.sql.SQLException;

public class AccountService {

    private final Dao<Account> accountDao;

    public AccountService(Dao<Account> accountDao) {
        this.accountDao = accountDao;
    }

    public Account addAccount(String accountNumber, BigDecimal amount) throws AccountException {
        Account account = new Account();
        account.setNumber(accountNumber);
        account.setAmount(amount);

        try {
            return accountDao.save(account);
        } catch (SQLException e) {
            throw new AccountException(e.getLocalizedMessage());
        }
    }

    public Account addAmount(Long accountId, BigDecimal sumValue) throws AccountException {
        Account account = accountDao.get(accountId)
                .orElseThrow(() -> new ArithmeticException(
                        "Account by id: " + accountId + " not found!"));
        try {
            account.setAmount(account.getAmount().add(sumValue));
            return accountDao.update(account);
        } catch (SQLException e) {
            throw new AccountException(e.getLocalizedMessage());
        }
    }

    public void deleteAccount(Long accountId) throws AccountException {
        try {
            accountDao.delete(accountId);
        } catch (SQLException e) {
            throw new AccountException(e.getLocalizedMessage());
        }
    }
}

package ru.outus.patterns.services;

import ru.outus.patterns.dao.Dao;
import ru.outus.patterns.entity.Account;

import java.math.BigDecimal;

public class AccountService {

    private final Dao<Account> accountDao;

    public AccountService(Dao<Account> accountDao) {
        this.accountDao = accountDao;
    }

    public Account addAccount(String accountNumber, BigDecimal amount) {
        Account account = new Account();
        account.setNumber(accountNumber);
        account.setAmount(amount);
        return accountDao.save(account);
    }

    public Account addAmount(Long accountId, BigDecimal sumValue) {
        Account account = accountDao.get(accountId).orElseThrow();

        BigDecimal newAmount = account.getAmount().add(sumValue);
        account.setAmount(newAmount);

        return accountDao.update(account);
    }
}

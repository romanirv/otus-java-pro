package ru.outus.patterns.dao.impl;

import ru.outus.patterns.dao.Dao;
import ru.outus.patterns.entity.Account;

import java.util.Optional;

public class AccountDao implements Dao<Account> {

    public AccountDao() {

    }

    @Override
    public Optional<Account> get(Long id) {
        return Optional.empty();
    }

    @Override
    public Account save(Account accountDao) {
        return null;
    }

    @Override
    public Account update(Account accountDao) {
        return null;
    }

    @Override
    public Account delete(Account accountDao) {
        return null;
    }
}

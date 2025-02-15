package ru.otus.java.pro.spring.app.services;

import ru.otus.java.pro.spring.app.entities.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    Optional<Account> getAccountById(String accountId, String clientId);
    List<Account> getAllAccountsByClientId(String clientId);
}

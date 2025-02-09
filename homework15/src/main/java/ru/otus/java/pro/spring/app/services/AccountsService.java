package ru.otus.java.pro.spring.app.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.java.pro.spring.app.entities.Account;
import ru.otus.java.pro.spring.app.repositories.AccountsRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountsService {

    private final AccountsRepository accountsRepository;

    public Optional<Account> getAccountById( String accountId, String clientId) {
        return accountsRepository.findByIdAndClientId(accountId, clientId);
    }

    public List<Account> getAllAccountsByClientId(String clientId) {
        return accountsRepository.findAccountByClientId(clientId);
    }
}

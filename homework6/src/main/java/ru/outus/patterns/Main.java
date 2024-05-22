package ru.outus.patterns;

import ru.outus.patterns.dao.impl.AccountDao;
import ru.outus.patterns.entity.Account;
import ru.outus.patterns.services.AccountService;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        // Create storage

        AccountService accountService = new AccountService(new AccountDao());

        Account account = accountService.addAccount("18293023", new BigDecimal("100.0"));
        account = accountService.addAmount(account.getId(), new BigDecimal("10.0"));

    }
}

package ru.otus.java.pro.spring.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.java.pro.spring.app.entities.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Account, String> {
    Optional<Account> findByIdAndClientId(String id, String clientId);

    Optional<Account> findByNumberAndClientId(String number, String clientId);

    List<Account> findAccountByClientId(String clientId);

    Account save(Account account);
}

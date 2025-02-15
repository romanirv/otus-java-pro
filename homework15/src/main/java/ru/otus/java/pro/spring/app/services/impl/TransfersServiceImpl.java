package ru.otus.java.pro.spring.app.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.pro.spring.app.dtos.ExecuteTransferDtoRq;
import ru.otus.java.pro.spring.app.entities.Account;
import ru.otus.java.pro.spring.app.entities.Transfer;
import ru.otus.java.pro.spring.app.exceptions_handling.BusinessLogicException;
import ru.otus.java.pro.spring.app.exceptions_handling.ValidationException;
import ru.otus.java.pro.spring.app.exceptions_handling.ValidationFieldError;
import ru.otus.java.pro.spring.app.repositories.AccountsRepository;
import ru.otus.java.pro.spring.app.repositories.TransfersRepository;
import ru.otus.java.pro.spring.app.services.TransfersService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransfersServiceImpl implements TransfersService {
    private final TransfersRepository transfersRepository;
    private final AccountsRepository accountsRepository;

    public Optional<Transfer> getTransferById(String id, String clientId) {
        return transfersRepository.findByIdAndClientId(id, clientId);
    }

    public List<Transfer> getAllTransfers(String clientId) {
        return transfersRepository.findByClientIdOrTargetClientId(clientId, clientId);
    }

    @Transactional
    public void execute(String clientId, ExecuteTransferDtoRq executeTransferDtoRq) {
        validateExecuteTransferDtoRq(executeTransferDtoRq);

        Account sourceAccount
                = getValidatedAccount
                    (executeTransferDtoRq.sourceAccount(),
                            clientId);
        Account targetAccount
                = getValidatedAccount
                    (executeTransferDtoRq.targetAccount(),
                            executeTransferDtoRq.targetClientId());

        checkTransferAmount(sourceAccount.getBalance(), executeTransferDtoRq.amount());

        int transferAmount = executeTransferDtoRq.amount();

        int sourceAccountBalance = sourceAccount.getBalance();
        sourceAccount.setBalance(sourceAccountBalance - transferAmount);

        int targetAccountBalance = targetAccount.getBalance();
        targetAccount.setBalance(targetAccountBalance + transferAmount);

        transfersRepository.save(new Transfer(
                UUID.randomUUID().toString(),
                clientId,
                executeTransferDtoRq.targetClientId(),
                sourceAccount.getNumber(), targetAccount.getNumber(),
                executeTransferDtoRq.message(),
                executeTransferDtoRq.amount()
        ));
    }

    private void validateExecuteTransferDtoRq(ExecuteTransferDtoRq executeTransferDtoRq) {
        List<ValidationFieldError> errors = new ArrayList<>();
        if (executeTransferDtoRq.sourceAccount().length() != 20) {
            errors.add(new ValidationFieldError("sourceAccount", "Длина поля счет отправителя должна составлять 12 символов"));
        }
        if (executeTransferDtoRq.targetAccount().length() != 20) {
            errors.add(new ValidationFieldError("targetAccount", "Длина поля счет получателя должна составлять 12 символов"));
        }
        if (executeTransferDtoRq.amount() <= 0) {
            errors.add(new ValidationFieldError("amount", "Сумма перевода должна быть больше 0"));
        }
        if (!errors.isEmpty()) {
            throw new ValidationException("EXECUTE_TRANSFER_VALIDATION_ERROR", "Проблемы заполнения полей перевода", errors);
        }
    }

    private Account getValidatedAccount(String accountNumber, String clientId) {
        Account account = accountsRepository
                .findByNumberAndClientId(accountNumber, clientId)
                .orElseThrow(() -> new BusinessLogicException(
                        "Account not found!", "EXECUTE_TRANSFER_ERROR"
                ));
        if (account.isBlocked()) {
            throw new BusinessLogicException("Account is blocked!", "EXECUTE_TRANSFER_ERROR");
        }
        return account;
    }

    private void checkTransferAmount(int sourceBalance, int transferAmount) {
        if (sourceBalance < transferAmount) {
            throw new BusinessLogicException("Insufficient funds in the account", "EXECUTE_TRANSFER_ERROR");
        }
    }
}

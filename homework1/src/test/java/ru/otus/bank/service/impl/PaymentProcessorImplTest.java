package ru.otus.bank.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.bank.entity.Account;
import ru.otus.bank.entity.Agreement;
import ru.otus.bank.service.AccountService;
import ru.otus.bank.service.exception.AccountException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentProcessorImplTest {

    @Mock
    AccountService accountService;

    @InjectMocks
    PaymentProcessorImpl paymentProcessor;

    @Test
    public void testTransfer() {
        Agreement sourceAgreement = new Agreement();
        sourceAgreement.setId(1L);

        Agreement destinationAgreement = new Agreement();
        destinationAgreement.setId(2L);

        Account sourceAccount = new Account();
        sourceAccount.setId(3L);
        sourceAccount.setAmount(BigDecimal.TEN);
        sourceAccount.setType(0);

        Account destinationAccount = new Account();
        destinationAccount.setId(4L);
        destinationAccount.setAmount(BigDecimal.ZERO);
        destinationAccount.setType(0);

        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 1L)))
                .thenReturn(List.of(sourceAccount));

        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 2L)))
                .thenReturn(List.of(destinationAccount));

        when(accountService.makeTransfer(eq(sourceAccount.getId()), eq(destinationAccount.getId()),
                eq(BigDecimal.ONE))).thenReturn(true);

        assertTrue(paymentProcessor.makeTransfer(sourceAgreement, destinationAgreement,
                0, 0, BigDecimal.ONE));
    }

    @Test
    public void testTransferSourceAccountNotFound() {
        Agreement sourceAgreement = new Agreement();
        sourceAgreement.setId(1L);

        Agreement destinationAgreement = new Agreement();
        destinationAgreement.setId(2L);

        Account sourceAccount = new Account();
        sourceAccount.setId(3L);
        sourceAccount.setAmount(BigDecimal.TEN);
        sourceAccount.setType(0);

        Account destinationAccount = new Account();
        destinationAccount.setId(4L);
        destinationAccount.setAmount(BigDecimal.ZERO);
        destinationAccount.setType(0);

        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 1L)))
                .thenReturn(List.of());

        AccountException ex = assertThrows(AccountException.class, () -> {
            paymentProcessor.makeTransfer(sourceAgreement, destinationAgreement,
                    0, 0, BigDecimal.ONE);
        });

        assertEquals("Account not found", ex.getLocalizedMessage());
    }

    @Test
    public void testTransferDestinationAccountNotFound() {
        Agreement sourceAgreement = new Agreement();
        sourceAgreement.setId(1L);

        Agreement destinationAgreement = new Agreement();
        destinationAgreement.setId(2L);

        Account sourceAccount = new Account();
        sourceAccount.setId(3L);
        sourceAccount.setAmount(BigDecimal.TEN);
        sourceAccount.setType(0);

        Account destinationAccount = new Account();
        destinationAccount.setId(4L);
        destinationAccount.setAmount(BigDecimal.ZERO);
        destinationAccount.setType(0);

        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 1L)))
                .thenReturn(List.of(sourceAccount));

        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 2L)))
                .thenReturn(List.of());

        AccountException ex = assertThrows(AccountException.class, () -> {
            paymentProcessor.makeTransfer(sourceAgreement, destinationAgreement,
                    0, 0, BigDecimal.ONE);
        });

        assertEquals("Account not found", ex.getLocalizedMessage());
    }

    @Test
    public void testMakeTransferWithComission() {
        Agreement sourceAgreement = new Agreement();
        sourceAgreement.setId(1L);

        Agreement destinationAgreement = new Agreement();
        destinationAgreement.setId(2L);

        Account sourceAccount = new Account();
        sourceAccount.setId(3L);
        sourceAccount.setAmount(BigDecimal.TEN);
        sourceAccount.setType(0);

        Account destinationAccount = new Account();
        destinationAccount.setId(4L);
        destinationAccount.setAmount(BigDecimal.ZERO);
        destinationAccount.setType(0);

        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 1L)))
                .thenReturn(List.of(sourceAccount));

        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 2L)))
                .thenReturn(List.of(destinationAccount));

        when(accountService.charge(eq(sourceAccount.getId()), eq(new BigDecimal("-0.1")))).thenReturn(true);

        when(accountService.makeTransfer(eq(sourceAccount.getId()), eq(destinationAccount.getId()),
                eq(BigDecimal.ONE))).thenReturn(true);

        assertTrue(paymentProcessor.makeTransferWithComission(sourceAgreement, destinationAgreement,
                0, 0, BigDecimal.ONE, new BigDecimal("0.1")));
    }

    @Test
    public void testMakeTransferWithComissionSrcAccountNotFound() {
        Agreement sourceAgreement = new Agreement();
        sourceAgreement.setId(1L);

        Agreement destinationAgreement = new Agreement();
        destinationAgreement.setId(2L);

        Account sourceAccount = new Account();
        sourceAccount.setId(3L);
        sourceAccount.setAmount(BigDecimal.TEN);
        sourceAccount.setType(0);

        Account destinationAccount = new Account();
        destinationAccount.setId(4L);
        destinationAccount.setAmount(BigDecimal.ZERO);
        destinationAccount.setType(0);

        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 1L)))
                .thenReturn(List.of());

        AccountException ex = assertThrows(AccountException.class, () -> {
            paymentProcessor.makeTransferWithComission(sourceAgreement, destinationAgreement,
                    0, 0, BigDecimal.ONE, new BigDecimal("0.1"));
        });

        assertEquals("Account not found", ex.getLocalizedMessage());
    }

    @Test
    public void testMakeTransferWithComissionDstAccountNotFound() {
        Agreement sourceAgreement = new Agreement();
        sourceAgreement.setId(1L);

        Agreement destinationAgreement = new Agreement();
        destinationAgreement.setId(2L);

        Account sourceAccount = new Account();
        sourceAccount.setId(3L);
        sourceAccount.setAmount(BigDecimal.TEN);
        sourceAccount.setType(0);

        Account destinationAccount = new Account();
        destinationAccount.setId(4L);
        destinationAccount.setAmount(BigDecimal.ZERO);
        destinationAccount.setType(0);

        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 1L)))
                .thenReturn(List.of(sourceAccount));

        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 2L)))
                .thenReturn(List.of());

        AccountException ex = assertThrows(AccountException.class, () -> {
            paymentProcessor.makeTransferWithComission(sourceAgreement, destinationAgreement,
                    0, 0, BigDecimal.ONE, new BigDecimal("0.1"));
        });

        assertEquals("Account not found", ex.getLocalizedMessage());
    }
}

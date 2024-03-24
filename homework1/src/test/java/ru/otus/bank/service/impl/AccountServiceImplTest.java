package ru.otus.bank.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.bank.dao.AccountDao;
import ru.otus.bank.entity.Account;
import ru.otus.bank.entity.Agreement;
import ru.otus.bank.service.exception.AccountException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    AccountDao accountDao;

    @InjectMocks
    AccountServiceImpl accountServiceImpl;

    @Test
    public void testAddAccount() {
        Agreement testAgreement = new Agreement();
        testAgreement.setId(1L);

        String testAccountNumber = "1";
        Integer testType = 1;
        BigDecimal testAmount = new BigDecimal(10);

        Account expectResult = new Account();
        expectResult.setId(10L);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        when(accountDao.save(accountArgumentCaptor.capture())).thenReturn(expectResult);

        Account result = accountServiceImpl.addAccount(testAgreement, testAccountNumber, testType, testAmount);

        assertEquals(testAgreement.getId(), accountArgumentCaptor.getValue().getAgreementId());
        assertEquals(testAccountNumber, accountArgumentCaptor.getValue().getNumber());
        assertEquals(testType, accountArgumentCaptor.getValue().getType());
        assertEquals(testAmount, accountArgumentCaptor.getValue().getAmount());

        assertEquals(expectResult.getId(), result.getId());
    }

    @Test
    void testGetAccounts() {
        ArrayList<Account> testAccounts = new ArrayList<>();

        Account testAccount1 = new Account();
        testAccount1.setId(1L);
        testAccounts.add(testAccount1);

        Account testAccount2 = new Account();
        testAccount2.setId(2L);
        testAccounts.add(testAccount2);

        Account testAccount3 = new Account();
        testAccount1.setId(3L);
        testAccounts.add(testAccount3);

        when(accountDao.findAll()).thenReturn(testAccounts);

        List<Account> resultAccounts = accountServiceImpl.getAccounts();

        assertEquals(testAccounts.size(), resultAccounts.size());
        assertTrue(resultAccounts.containsAll(testAccounts));
    }

    @Test
    void testGetAccountsForAgreement() {
        ArrayList<Account> testAccounts = new ArrayList<>();

        Account testAccount1 = new Account();
        testAccount1.setId(1L);
        testAccount1.setAgreementId(1L);

        testAccounts.add(testAccount1);

        Agreement testAgreement = new Agreement();
        testAgreement.setId(1L);

        when(accountDao.findByAgreementId(testAgreement.getId())).thenReturn(testAccounts);

        List<Account> resultAccounts = accountServiceImpl.getAccounts(testAgreement);

        assertEquals(testAccounts.size(), resultAccounts.size());
        assertTrue(resultAccounts.containsAll(testAccounts));
    }

    @Test
    void testAccountChange() {
        BigDecimal testChangeAmount = new BigDecimal("100.0");

        Account testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setAmount(new BigDecimal("150.1"));
        testAccount.setId(1L);

        when(accountDao.findById(testAccount.getId())).thenReturn(Optional.of(testAccount));
        when(accountDao.save(testAccount)).thenReturn(testAccount);

        assertTrue(accountServiceImpl.charge(testAccount.getId(), testChangeAmount));
        assertEquals(new BigDecimal("50.1"), testAccount.getAmount());
    }

    @Test
    void testNotExistAccount() {
        Long testAccountId = 1L;
        BigDecimal testChangeAmount = new BigDecimal("100.0");

        when(accountDao.findById(testAccountId)).thenReturn(Optional.empty());

        AccountException ex = assertThrows(AccountException.class, () -> {
            accountServiceImpl.charge(testAccountId, testChangeAmount);
        });

        assertEquals("No source account", ex.getLocalizedMessage());
    }

    @Test
    public void testTransfer() {
        Account sourceAccount = new Account();
        sourceAccount.setAmount(new BigDecimal(100));

        Account destinationAccount = new Account();
        destinationAccount.setAmount(new BigDecimal(10));

        when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
        when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

        accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));

        assertEquals(new BigDecimal(90), sourceAccount.getAmount());
        assertEquals(new BigDecimal(20), destinationAccount.getAmount());
    }

    @Test
    public void testSourceNotFound() {
        when(accountDao.findById(any())).thenReturn(Optional.empty());

        AccountException result = assertThrows(AccountException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));
            }
        });
        assertEquals("No source account", result.getLocalizedMessage());
    }


    @Test
    public void testTransferWithVerify() {
        Account sourceAccount = new Account();
        sourceAccount.setAmount(new BigDecimal(100));
        sourceAccount.setId(1L);

        Account destinationAccount = new Account();
        destinationAccount.setAmount(new BigDecimal(10));
        destinationAccount.setId(2L);

        when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
        when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

        ArgumentMatcher<Account> sourceMatcher =
                argument -> argument.getId().equals(1L) && argument.getAmount().equals(new BigDecimal(90));

        ArgumentMatcher<Account> destinationMatcher =
                argument -> argument.getId().equals(2L) && argument.getAmount().equals(new BigDecimal(20));

        accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));

        verify(accountDao).save(argThat(sourceMatcher));
        verify(accountDao).save(argThat(destinationMatcher));
    }

    @Test
    public void testTransferWhenSumMoreThanBalance() {
        Account sourceAccount = new Account();
        sourceAccount.setAmount(new BigDecimal(10));
        sourceAccount.setId(1L);

        Account destinationAccount = new Account();
        destinationAccount.setAmount(new BigDecimal(10));
        destinationAccount.setId(2L);

        when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
        when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

        assertFalse(accountServiceImpl.makeTransfer(sourceAccount.getId(),
                destinationAccount.getId(), new BigDecimal(100)));
    }

    @Test
    public void testTransferWhenSumIsZero() {
        Account sourceAccount = new Account();
        sourceAccount.setAmount(new BigDecimal(10));
        sourceAccount.setId(1L);

        Account destinationAccount = new Account();
        destinationAccount.setAmount(new BigDecimal(10));
        destinationAccount.setId(2L);

        when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
        when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

        assertFalse(accountServiceImpl.makeTransfer(sourceAccount.getId(),
                destinationAccount.getId(), new BigDecimal(0)));
    }
}

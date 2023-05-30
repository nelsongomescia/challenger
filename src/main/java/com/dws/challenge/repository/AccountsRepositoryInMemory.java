package com.dws.challenge.repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.exception.InvalidAccountIdException;

import lombok.Synchronized;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final AtomicReference<Account> accountAR = new AtomicReference<>();

    @Override
    public void createAccount(Account account) throws DuplicateAccountIdException {

        Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
        if (previousAccount != null) {
            throw new DuplicateAccountIdException(
                    "Account id " + account.getAccountId() + " already exists!");
        }
    }

    @Override
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    @Override
    public void clearAccounts() {
        accounts.clear();
    }

    @Override
    public void updateAccount(
            String accountId,
            BigDecimal transferAmount,
            boolean isACreditOperation) throws DuplicateAccountIdException {

        Account actualAccount = accounts.get(accountId);
        if (actualAccount == null) {
            throw new InvalidAccountIdException(
                    "Account id " + accountId + " didn´t exists!");
        }

        accountAR.set(actualAccount);
        accountAR.updateAndGet(accountA -> {
            if (isACreditOperation) {
                actualAccount.setBalance(actualAccount.getBalance().add(transferAmount));
            } else {
                actualAccount.setBalance(actualAccount.getBalance().subtract(transferAmount));
            }
            return actualAccount;
        });
        accounts.put(actualAccount.getAccountId(), actualAccount);

    }

}

package com.dws.challenge.repository;

import java.math.BigDecimal;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;

public interface AccountsRepository {

  void createAccount(Account account) throws DuplicateAccountIdException;

  Account getAccount(String accountId);

  void clearAccounts();

  void updateAccount(
          String accountId,
          BigDecimal transferAmount,
          boolean isACreditOperation) throws DuplicateAccountIdException;
}

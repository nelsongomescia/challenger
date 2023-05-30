package com.dws.challenge.service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.TransferMoneyDTO;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.service.interfaces.IAccountValidationServices;
import com.dws.challenge.service.interfaces.IAccountsService;
import com.dws.challenge.service.interfaces.INotificationService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

@Service
@RequiredArgsConstructor
public class AccountsService implements IAccountsService {


  @Getter
  private final AccountsRepository accountsRepository;
  private final INotificationService INotificationService;
  private final IAccountValidationServices accountValidationServices;
  private ReentrantLock lock = new ReentrantLock();

  @Override
  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  @Override
  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }

  @Override
  public TransferMoneyDTO transferMoney(TransferMoneyDTO transferMoneyDTO) {

        //Let´s get Both Accounts
        Account toAccount = getAccount(transferMoneyDTO.getToAccountId());
        Account fromAccount = getAccount(transferMoneyDTO.getFromAccountId());

        //Validate if Accounts and Money Transfer are valid
        accountValidationServices.isAValidTransferProcess(toAccount, fromAccount, transferMoneyDTO);

        //Persist New Data on DB, with new Balances
        accountsRepository.updateAccount(toAccount.getAccountId(), transferMoneyDTO.getTransferAmount(), true);
        accountsRepository.updateAccount(fromAccount.getAccountId(), transferMoneyDTO.getTransferAmount(), false);

        //Inform both Accounts Using Notification Services
        INotificationService.notifyAboutTransfer(toAccount,
                String.format("Transfer %s coins from %s account", transferMoneyDTO.getTransferAmount(), transferMoneyDTO.getFromAccountId()));
        INotificationService.notifyAboutTransfer(fromAccount,
                String.format("Transfer %s coins to %s account", transferMoneyDTO.getTransferAmount(), transferMoneyDTO.getToAccountId()));


    return transferMoneyDTO;

  }




}

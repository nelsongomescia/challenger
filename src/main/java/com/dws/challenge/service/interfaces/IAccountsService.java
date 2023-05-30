package com.dws.challenge.service.interfaces;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.TransferMoneyDTO;

public interface IAccountsService {
	void createAccount(Account account);

	Account getAccount(String accountId);

	TransferMoneyDTO transferMoney(TransferMoneyDTO transferMoneyDTO);
}

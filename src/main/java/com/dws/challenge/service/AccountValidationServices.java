package com.dws.challenge.service;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.TransferMoneyDTO;
import com.dws.challenge.exception.InvalidAccountIdException;
import com.dws.challenge.exception.InvalidMoneyTransferException;
import com.dws.challenge.service.interfaces.IAccountValidationServices;

@Service
public class AccountValidationServices implements IAccountValidationServices {

	public static final String INVALID_ACCOUNT_WITH_ID_S_DETECTED = "Invalid Account with Id: %s detected.";
	public static final String FROM_ACCOUNT_EQUALS_TO_ACCOUNT = "From Account and To Account are the same %s.";
	public static final String THERE_IS_NOT_ENOUGH_MONEY_TO_PERFORM_THIS_TRANSFER_NECESSARY_S_AVAILABLE_S = "There is not enough Money to perform this Transfer. Necessary: %s - Available: %s.";

	@Override
	public void isAValidTransferProcess(
			Account toAccount,
			Account fromAccount,
			TransferMoneyDTO transferMoneyDTO) {

		if (isAValidAccount.negate().test(toAccount)) {
			throw new InvalidAccountIdException(String.format(
					INVALID_ACCOUNT_WITH_ID_S_DETECTED, transferMoneyDTO.getToAccountId()));
		}

		if (isAValidAccount.negate().test(fromAccount)) {
			throw new InvalidAccountIdException(String.format(
					INVALID_ACCOUNT_WITH_ID_S_DETECTED, transferMoneyDTO.getFromAccountId()));
		}

		if (areTheSameAccount.test(toAccount, fromAccount)) {
			throw new InvalidAccountIdException(String.format(
					FROM_ACCOUNT_EQUALS_TO_ACCOUNT, transferMoneyDTO.getFromAccountId()));
		}

		if (thereIsEnoughMoneyInAccount.negate().test(fromAccount, transferMoneyDTO)) {
			throw new InvalidMoneyTransferException(String.format(
					THERE_IS_NOT_ENOUGH_MONEY_TO_PERFORM_THIS_TRANSFER_NECESSARY_S_AVAILABLE_S, transferMoneyDTO.getTransferAmount(), fromAccount.getBalance()));
		}

	}

	private BiPredicate<Account, Account> areTheSameAccount = (toAccount, fromAccount) -> toAccount.getAccountId().equalsIgnoreCase(fromAccount.getAccountId());

	private Predicate<Account> isAValidAccount  = account -> account != null && !account.getAccountId().isBlank();

	private BiPredicate<Account, TransferMoneyDTO> thereIsEnoughMoneyInAccount = (account, transferMoneyDTO) ->
			account.getBalance().compareTo(transferMoneyDTO.getTransferAmount()) >= 0;


}

package com.dws.challenge.service.interfaces;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.TransferMoneyDTO;

public interface IAccountValidationServices {
	void isAValidTransferProcess(
			Account toAccount,
			Account fromAccount,
			TransferMoneyDTO transferMoneyDTO);
}

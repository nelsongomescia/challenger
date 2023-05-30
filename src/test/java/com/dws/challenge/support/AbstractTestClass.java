package com.dws.challenge.support;

import java.math.BigDecimal;
import java.util.UUID;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.TransferMoneyDTO;

public class AbstractTestClass {

	public String toAccountId = UUID.randomUUID().toString();
	public String fromAccountId = UUID.randomUUID().toString();

	public BigDecimal toAccountBalance = new BigDecimal(1000);
	public BigDecimal fromAccountBalance = new BigDecimal(1000);
	public BigDecimal validTransferAmount = new BigDecimal(500);
	public BigDecimal validConcurrencyTransferAmount = new BigDecimal(1);
	public BigDecimal invalidTransferAmount = new BigDecimal(2000);

	public class TestExamples {

		public Account validToAccount;
		public Account validFromAccount;
		public TransferMoneyDTO validTransferMoneyDTO;
		public TransferMoneyDTO validTransferMoneyConcurrencyDTO;
		public TransferMoneyDTO invalidTransferMoneyAmountDTO;

		public TestExamples() {

			validToAccount = new Account(toAccountId, toAccountBalance);
			validFromAccount = new Account(fromAccountId, fromAccountBalance);
			validTransferMoneyDTO = TransferMoneyDTO.builder()
					.fromAccountId(fromAccountId)
					.toAccountId(toAccountId)
					.transferAmount(validTransferAmount)
					.build();
			validTransferMoneyConcurrencyDTO = TransferMoneyDTO.builder()
					.fromAccountId(fromAccountId)
					.toAccountId(toAccountId)
					.transferAmount(validConcurrencyTransferAmount)
					.build();
			invalidTransferMoneyAmountDTO = TransferMoneyDTO.builder()
					.fromAccountId(fromAccountId)
					.toAccountId(toAccountId)
					.transferAmount(invalidTransferAmount)
					.build();

		}

	}


}

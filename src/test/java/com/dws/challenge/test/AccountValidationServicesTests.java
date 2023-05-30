package com.dws.challenge.test;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dws.challenge.exception.InvalidAccountIdException;
import com.dws.challenge.exception.InvalidMoneyTransferException;
import com.dws.challenge.service.interfaces.IAccountValidationServices;
import com.dws.challenge.support.AbstractTestClass;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountValidationServicesTests extends AbstractTestClass {

	private TestExamples src = new TestExamples();

	@Autowired
	private IAccountValidationServices accountValidationServices;

	@Test
	void invalidToAccount() {
		Assertions.assertThrows(InvalidAccountIdException.class, () ->
				accountValidationServices.isAValidTransferProcess(null, src.validFromAccount, src.validTransferMoneyDTO));
	}

	@Test
	void invalidFromAccount() {
		Assertions.assertThrows(InvalidAccountIdException.class, () ->
				accountValidationServices.isAValidTransferProcess(src.validToAccount, null, src.validTransferMoneyDTO));
	}

	@Test
	void toAccountEqualsFromAccount() {
		Assertions.assertThrows(InvalidAccountIdException.class, () ->
				accountValidationServices.isAValidTransferProcess(src.validToAccount, src.validToAccount, src.validTransferMoneyDTO));
	}

	@Test
	void notEnoughMoneyAvailable() {
		Assertions.assertThrows(InvalidMoneyTransferException.class, () ->
				accountValidationServices.isAValidTransferProcess(src.validToAccount, src.validFromAccount, src.invalidTransferMoneyAmountDTO));
	}

	@Test
	void isAValidMoneyTransfer() {

		try {
			accountValidationServices.isAValidTransferProcess(
					src.validToAccount,
					src.validFromAccount,
					src.validTransferMoneyDTO);

		} catch (Throwable throwable) {
			fail();
		}

	}

}

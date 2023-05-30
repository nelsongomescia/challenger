package com.dws.challenge.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.InvalidMoneyTransferException;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.EmailINotificationService;
import com.dws.challenge.support.AbstractTestClass;

@SpringBootTest
class ChallengeApplicationTests extends AbstractTestClass {

	private TestExamples src = new TestExamples();

	@Autowired
	private AccountsService accountsService;

	@MockBean
	private EmailINotificationService emailINotificationService;

	@Test
	void invalidAccountTransferMoney() {

		//First Create To Account
		this.accountsService.createAccount(src.validToAccount);

		//Second Create From Account
		this.accountsService.createAccount(src.validFromAccount);

		//Process Money Transfer
		ExecutorService service = Executors.newFixedThreadPool(1);
		CountDownLatch latch = new CountDownLatch(1);
		service.execute(() -> {

			Assertions.assertThrows(InvalidMoneyTransferException.class, () ->
					this.accountsService.transferMoney(src.invalidTransferMoneyAmountDTO));

			latch.countDown();

		});

	}


}

package com.dws.challenge.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.TransferMoneyDTO;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.EmailINotificationService;
import com.dws.challenge.support.AbstractTestClass;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
class AccountsControllerTest extends AbstractTestClass {

  private TestExamples src = new TestExamples();

  private MockMvc mockMvc;

  @MockBean
  private EmailINotificationService emailINotificationService;

  @Autowired
  private AccountsService accountsService;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private WebApplicationContext webApplicationContext;


  @BeforeEach
  void prepareMockMvc() {
    this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

    // Reset the existing accounts before each test.
    accountsService.getAccountsRepository().clearAccounts();
  }

  @Test
  void createAccount() throws Exception {
    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
      .content("{\"accountId\":\"Id-123\",\"balance\":1000}")).andExpect(status().isCreated());

    Account account = accountsService.getAccount("Id-123");
    assertThat(account.getAccountId()).isEqualTo("Id-123");
    assertThat(account.getBalance()).isEqualByComparingTo("1000");
  }

  @Test
  void createDuplicateAccount() throws Exception {
    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
      .content("{\"accountId\":\"Id-123\",\"balance\":1000}")).andExpect(status().isCreated());

    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
      .content("{\"accountId\":\"Id-123\",\"balance\":1000}")).andExpect(status().isBadRequest());
  }

  @Test
  void createAccountNoAccountId() throws Exception {
    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
      .content("{\"balance\":1000}")).andExpect(status().isBadRequest());
  }

  @Test
  void createAccountNoBalance() throws Exception {
    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
      .content("{\"accountId\":\"Id-123\"}")).andExpect(status().isBadRequest());
  }

  @Test
  void createAccountNoBody() throws Exception {
    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAccountNegativeBalance() throws Exception {
    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
      .content("{\"accountId\":\"Id-123\",\"balance\":-1000}")).andExpect(status().isBadRequest());
  }

  @Test
  void createAccountEmptyAccountId() throws Exception {
    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
      .content("{\"accountId\":\"\",\"balance\":1000}")).andExpect(status().isBadRequest());
  }

  @Test
  void getAccount() throws Exception {
    String uniqueAccountId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueAccountId, new BigDecimal("123.45"));
    this.accountsService.createAccount(account);
    this.mockMvc.perform(get("/v1/accounts/" + uniqueAccountId))
      .andExpect(status().isOk())
      .andExpect(
        content().string("{\"accountId\":\"" + uniqueAccountId + "\",\"balance\":123.45}"));
  }


  @Test
  void transferMoneyProcessSuccessfully() throws Exception {

    //Mocking Beans
    doNothing().when(emailINotificationService).notifyAboutTransfer(any(), any());

    //Expected Values
    BigDecimal expectAmountInToAccount = src.validToAccount.getBalance().add(src.validTransferMoneyDTO.getTransferAmount());
    BigDecimal expectAmountInFromAccount = src.validFromAccount.getBalance().subtract(src.validTransferMoneyDTO.getTransferAmount());

    //First Create To Account
    this.accountsService.createAccount(src.validToAccount);

    //Second Create From Account
    this.accountsService.createAccount(src.validFromAccount);

    //Perform Task
    this.mockMvc.perform(post("/v1/accounts/transferMoney")
                    .content(mapper.writeValueAsString(src.validTransferMoneyDTO))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(
            content()
                    .string("{\"fromAccountId\":\"" + src.validTransferMoneyDTO.getFromAccountId()
                            + "\",\"toAccountId\":\"" + src.validTransferMoneyDTO.getToAccountId()
                            + "\",\"transferAmount\":" + src.validTransferMoneyDTO.getTransferAmount() + "}"));

    //Getting Update Accounts to Validate Transfer
    Account updateToAccount = this.accountsService.getAccount(src.validToAccount.getAccountId());
    Account updateFromAccount = this.accountsService.getAccount(src.validFromAccount.getAccountId());

    //Validate Transfer Process
    Assertions.assertEquals(expectAmountInFromAccount, updateFromAccount.getBalance());
    Assertions.assertEquals(expectAmountInToAccount, updateToAccount.getBalance());

    //Validate if the Mock has been called
    Mockito.verify(emailINotificationService, Mockito.times(2))
            .notifyAboutTransfer(any(), any());

  }


  @Test
  void transferMoneyProcessWrongDTO() throws Exception {

    TransferMoneyDTO validTransferMoneyDTO = src.validTransferMoneyDTO;
    validTransferMoneyDTO.setToAccountId(null);

    this.mockMvc.perform(post("/v1/accounts/transferMoney")
                    .content(mapper.writeValueAsString(src.validTransferMoneyDTO))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(
                    content()
                            .string(org.hamcrest.Matchers.containsString("\"toAccountId\":\"" + "must not be")));

  }


}

package com.dws.challenge.web;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dws.challenge.common.predicates.GenericPredicates;
import com.dws.challenge.common.validations.BindingResultValidation;
import com.dws.challenge.domain.Account;
import com.dws.challenge.domain.ResponseDTO;
import com.dws.challenge.domain.TransferMoneyDTO;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.interfaces.IAccountsService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

  private final IAccountsService accountsService;

  @Autowired
  public AccountsController(AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
    log.info("Creating account {}", account);

    try {
    this.accountsService.createAccount(account);
    } catch (DuplicateAccountIdException daie) {
      return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping(path = "/{accountId}")
  public Account getAccount(@PathVariable String accountId) {
    log.info("Retrieving account for id {}", accountId);
    return this.accountsService.getAccount(accountId);
  }

  @PostMapping(path = "/transferMoney", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> updateItem(@Valid @RequestBody TransferMoneyDTO transferMoneyDTO, BindingResult bindingResult) {

    ResponseEntity<ResponseDTO> responseResponseEntity = BindingResultValidation.validateBindingResult(bindingResult);
    if (GenericPredicates.checkIfNullOrEmpty.negate().test(responseResponseEntity)) return badRequest().body(responseResponseEntity);

    log.info("Start Transfer Money from Account {} to Account {}, amount {}.",
            transferMoneyDTO.getFromAccountId(),
            transferMoneyDTO.getToAccountId(),
            transferMoneyDTO.getTransferAmount());

    return ok().body(this.accountsService.transferMoney(transferMoneyDTO));
  }

}

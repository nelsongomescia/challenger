package com.dws.challenge.service.interfaces;

import com.dws.challenge.domain.Account;

public interface INotificationService {

  void notifyAboutTransfer(Account account, String transferDescription);
}

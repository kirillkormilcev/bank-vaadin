package dev.kormilcev.bank.integration.service;

import dev.kormilcev.bank.integration.BaseIntegrationTest;
import dev.kormilcev.bank.model.dto.AccountResponse;
import dev.kormilcev.bank.model.dto.NewAccountRequest;
import dev.kormilcev.bank.service.AccountService;
import dev.kormilcev.bank.service.impl.AccountServiceImpl;
import dev.kormilcev.bank.util.TestDataFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AccountServiceTest extends BaseIntegrationTest {
  AccountService accountService = AccountServiceImpl.getInstance();

  @Test
  @DisplayName("is account create ok")
  public void isAccountCreateOk() {
    NewAccountRequest request = TestDataFactory.request;

    AccountResponse response = accountService.create(request);

    Assertions.assertEquals(response.bic(), request.bic());
  }
}

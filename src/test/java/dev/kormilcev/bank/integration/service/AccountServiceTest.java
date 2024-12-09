package dev.kormilcev.bank.integration.service;

import dev.kormilcev.bank.model.dto.AccountResponse;
import dev.kormilcev.bank.model.dto.ClientResponse;
import dev.kormilcev.bank.model.dto.NewAccountRequest;
import dev.kormilcev.bank.model.dto.NewClientRequest;
import dev.kormilcev.bank.service.AccountService;
import dev.kormilcev.bank.service.ClientService;
import dev.kormilcev.bank.service.impl.AccountServiceImpl;
import dev.kormilcev.bank.service.impl.ClientServiceImpl;
import dev.kormilcev.bank.util.TestDataFactory;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Тест запускать на инициализированной базе с пустыми таблицами account, client
 * */
public class AccountServiceTest{
  AccountService accountService = AccountServiceImpl.getInstance();
  ClientService clientService = ClientServiceImpl.getInstance();

  @Test
  @DisplayName("concurrency test")
  void concurrencyTest() throws InterruptedException{
    // клиент 1
    NewClientRequest newClientRequest1 = TestDataFactory.newClientRequest1;
    // сохраняем клиента 1
    ClientResponse clientResponse1 = clientService.createClient(newClientRequest1);
    // клиент 2
    NewClientRequest newClientRequest2 = TestDataFactory.newClientRequest2;
    // сохраняем клиента 2
    ClientResponse clientResponse2 = clientService.createClient(newClientRequest2);
    // счет для клиента 1
    NewAccountRequest newAccountRequest1 = NewAccountRequest.builder()
        .bic("123456789")
        .currency("RUB")
        .clientId(clientResponse1.clientId())
        .build();
    // сохраняем счет для клиента 1
    AccountResponse accountResponse1 = accountService.create(newAccountRequest1);
    // пополняем счет на 2000 клиента 1
    accountService.replenish(accountResponse1.paymentAccount(), BigDecimal.valueOf(2000L));
    // обновляем баланс счета клиента 1
    AccountResponse accountResponse1Updated = accountService.getAllOpenAccountsByClientId(
        accountResponse1.clientId()).get(0);

    Assertions.assertEquals(BigDecimal.valueOf(2000L), accountResponse1Updated.balance());
    // счет для клиента 2
    NewAccountRequest newAccountRequest2 = NewAccountRequest.builder()
        .bic("123456789")
        .currency("RUB")
        .clientId(clientResponse2.clientId())
        .build();
    // сохраняем счет клиента 2 с балансом 0
    AccountResponse accountResponse2 = accountService.create(newAccountRequest2);

    // запускаем в многопоточной среде
    CountDownLatch latch = new CountDownLatch(1000);

    ExecutorService executor = Executors.newFixedThreadPool(100);

    for (int i = 0; i < 500; i++) {
      AccountResponse finalAccountResponse1 = accountResponse1Updated;
      AccountResponse finalAccountResponse2 = accountResponse2;

      executor.submit(() -> {
        try {
          accountService.transfer(
              finalAccountResponse1.paymentAccount(),
              finalAccountResponse1.balance(),
              BigDecimal.ONE,
              finalAccountResponse2.paymentAccount());
        } finally {
          latch.countDown();
        }
      });

      accountResponse1Updated = accountService.getAllOpenAccountsByClientId(accountResponse1.clientId()).get(0);

      AccountResponse final2AccountResponse1 = accountResponse1Updated;
      AccountResponse final2AccountResponse2 = accountResponse2;


      executor.submit(() -> {
        try {
          accountService.transfer(
              final2AccountResponse1.paymentAccount(),
              final2AccountResponse1.balance(),
              BigDecimal.ONE,
              final2AccountResponse2.paymentAccount());
        } finally {
          latch.countDown();
        }
      });

      accountResponse1Updated = accountService.getAllOpenAccountsByClientId(accountResponse1.clientId()).get(0);
    }

    executor.shutdown();

    latch.await();

    // обновляем баланс счета клиента 1
    accountResponse1Updated = accountService.getAllOpenAccountsByClientId(accountResponse1.clientId()).get(0);

    Assertions.assertEquals(BigDecimal.valueOf(1000L), accountResponse1Updated.balance());

    // обновляем баланс счета клиента 2
    accountResponse2 = accountService.getAllOpenAccountsByClientId(accountResponse2.clientId()).get(0);

    Assertions.assertEquals(BigDecimal.valueOf(1000L), accountResponse2.balance());
  }
}

package dev.kormilcev.bank.service;

import dev.kormilcev.bank.model.Currency;
import dev.kormilcev.bank.model.dto.AccountResponse;
import dev.kormilcev.bank.model.dto.NewAccountRequest;
import dev.kormilcev.bank.model.dto.UpdateAccountRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {

  AccountResponse create(NewAccountRequest request);

  Optional<AccountResponse> update(UpdateAccountRequest request);

  List<AccountResponse> getAllOpenAccountsByClientId(Long clientId);

  boolean closeAccount(String paymentAccount);

  List<AccountResponse> getAllAvailableAccounts(String paymentAccount);

  boolean transfer(String fromPaymentAccount, BigDecimal balance, BigDecimal amount,
      String toPaymentAccount);

  boolean replenish(String paymentAccount, BigDecimal amount);

  List<Currency> getCurrencies();
}

package dev.kormilcev.bank.service.impl;

import dev.kormilcev.bank.exception.DataBaseStatementException;
import dev.kormilcev.bank.model.Account;
import dev.kormilcev.bank.model.AccountStatus;
import dev.kormilcev.bank.model.Currency;
import dev.kormilcev.bank.model.dto.AccountResponse;
import dev.kormilcev.bank.model.dto.NewAccountRequest;
import dev.kormilcev.bank.model.dto.UpdateAccountRequest;
import dev.kormilcev.bank.repository.AccountDao;
import dev.kormilcev.bank.repository.impl.AccountDaoImpl;
import dev.kormilcev.bank.service.AccountService;
import dev.kormilcev.bank.service.mapper.AccountMapper;
import dev.kormilcev.bank.validation.AccountValidator;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountServiceImpl implements AccountService {

  final AccountDao accountDao = AccountDaoImpl.getInstance();
  static final AccountMapper accountMapper = AccountMapper.INSTANCE;

  static AccountService instance;

  public static synchronized AccountService getInstance() {
    if (instance == null) {
      instance = new AccountServiceImpl();
    }
    return instance;
  }

  @Override
  public AccountResponse create(NewAccountRequest request) {
    Account account = accountMapper.newAccountRequestToClient(request);

    account.setBalance(BigDecimal.ZERO);
    account.setStatus(AccountStatus.OPEN);

    //заглушка
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 20; i++) {
      sb.append(random.nextInt(10));
    }

    account.setPaymentAccount(sb.toString());

    return accountMapper.accountToAccountResponse(
        accountDao.create(account));
  }

  public Optional<AccountResponse> update(UpdateAccountRequest request) {
    return Optional.ofNullable(accountMapper.accountToAccountResponse(
        accountDao.update(accountMapper.updateAccountRequestToClient(request))));
  }

  @Override
  public List<AccountResponse> getAllOpenAccountsByClientId(Long clientId) {
    return accountMapper.accountListToAccountResponseList(accountDao.findOpenByClientId(clientId));
  }

  @Override
  public boolean closeAccount(String paymentAccount) {
    return accountDao.close(paymentAccount);
  }

  @Override
  public List<AccountResponse> getAllAvailableAccounts(String paymentAccount) {
    return accountMapper.accountListToAccountResponseList(
        accountDao.findAllExceptCurrent(paymentAccount));
  }

  @Override
  public boolean transfer(String fromPaymentAccount, BigDecimal balance, BigDecimal amount,
      String toPaymentAccount) {
    AccountValidator.throwIfTransferNotValid(balance, amount);
    try {
      return accountDao.transfer(fromPaymentAccount, balance, amount, toPaymentAccount);
    } catch (SQLException e) {
      throw new DataBaseStatementException("Ошибка при переводе средств на уровне сервиса."
          + System.lineSeparator() + e.getMessage());
    }
  }

  @Override
  public boolean replenish(String paymentAccount, BigDecimal amount) {
    return accountDao.replenish(paymentAccount, amount);
  }

  @Override
  public List<Currency> getCurrencies() {
    return accountDao.getCurrencies();
  }
}

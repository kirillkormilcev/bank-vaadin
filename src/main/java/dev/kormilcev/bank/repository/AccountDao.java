package dev.kormilcev.bank.repository;

import dev.kormilcev.bank.model.Account;
import dev.kormilcev.bank.model.Currency;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface AccountDao extends Dao<Account, Long> {

  boolean close(String paymentAccount);

  boolean transfer(String fromPaymentAccount, BigDecimal balance, BigDecimal amount,
      String toPaymentAccount)
      throws SQLException;

  boolean replenish(String paymentAccount, BigDecimal amount);

  List<Account> findOpenByClientId(Long clientId);

  List<Account> findAllExceptCurrent(String paymentAccount);

  List<Currency> getCurrencies();
}

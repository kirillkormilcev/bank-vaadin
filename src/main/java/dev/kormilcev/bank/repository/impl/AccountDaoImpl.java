package dev.kormilcev.bank.repository.impl;

import dev.kormilcev.bank.db.ConnectionManager;
import dev.kormilcev.bank.db.ConnectionManagerImpl;
import dev.kormilcev.bank.exception.DataBaseStatementException;
import dev.kormilcev.bank.model.Account;
import dev.kormilcev.bank.model.AccountStatus;
import dev.kormilcev.bank.model.Currency;
import dev.kormilcev.bank.repository.AccountDao;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDaoImpl implements AccountDao {

  private static final String SAVE_SQL = """
            INSERT INTO account (payment_account, balance, status, bic, currency, client_id)
            VALUES (?, ?, ?, ?, ?, ?);
            """;

  private static final String FIND_ALL_CURRENCY_SQL = """
            SELECT currency_id, c.currency, label, disabled FROM currency c;
            """;

  private static final String CLOSE_SQL = """
            UPDATE account
            SET status = ?
            WHERE payment_account = ?;
            """;

  private static final String FIND_ALL_OPEN_BY_CLIENT_ID = """
            SELECT account_id, payment_account, balance, status, bic, currency, client_id  FROM account
            WHERE status = ?
            AND client_id = ?;
            """;

  private static final String FIND_ALL_EXCEPT_CURRENT = """
            SELECT account_id, payment_account, balance, status, bic, currency, client_id  FROM account
            WHERE payment_account != ?
            AND status = ?;
            """;

  private static final String GET_BALANCE_SQL = """
            SELECT balance FROM account
            WHERE payment_account = ?;
            """;

  private static final String UPDATE_BALANCE_SQL = """
            UPDATE account
            SET balance = balance + ?
            WHERE payment_account = ?;
            """;

  private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
  private static AccountDao instance;

  public static  synchronized AccountDao getInstance() {
    if (instance == null) {
      instance = new AccountDaoImpl();
    }
    return instance;
  }

  @Override
  public Account create(Account account) {
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

      preparedStatement.setString(1, account.getPaymentAccount());
      preparedStatement.setBigDecimal(2, account.getBalance());
      preparedStatement.setString(3, account.getStatus().name());
      preparedStatement.setString(4, account.getBic());
      preparedStatement.setString(5, account.getCurrency());
      preparedStatement.setLong(6, account.getClientId());

      preparedStatement.executeUpdate();

      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet.next()) {
        account = new Account(
            resultSet.getLong("account_id"),
            account.getPaymentAccount(),
            account.getBalance(),
            account.getStatus(),
            account.getBic(),
            account.getCurrency(),
            account.getClientId()
        );
      }
    } catch (SQLException e) {
      throw new DataBaseStatementException("Ошибка при создании счета в базе данных."
          + System.lineSeparator() + e.getMessage());
    }
    return account;
  }

  @Override
  public Account update(Account account) {
    return null;
  }

  @Override
  public boolean close(String paymentAccount) {
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(CLOSE_SQL)) {

      preparedStatement.setString(1, AccountStatus.CLOSED.name());
      preparedStatement.setString(2, paymentAccount);

      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException e) {
      throw new DataBaseStatementException("Ошибка при закрытии счета."
          + System.lineSeparator() + e.getMessage());
    }
  }

  @Override
  public boolean transfer(String fromPaymentAccount, BigDecimal balance, BigDecimal amount, String toPaymentAccount)
      throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatementUpdateFrom = null;
    PreparedStatement preparedStatementUpdateTo = null;
    try {
      connection = connectionManager.getConnection();
      connection.setAutoCommit(false);

      preparedStatementUpdateFrom = connection.prepareStatement(UPDATE_BALANCE_SQL);
      preparedStatementUpdateFrom.setBigDecimal(1, amount.negate());
      preparedStatementUpdateFrom.setString(2, fromPaymentAccount);
      preparedStatementUpdateFrom.executeUpdate();

      preparedStatementUpdateTo = connection.prepareStatement(UPDATE_BALANCE_SQL);
      preparedStatementUpdateTo.setBigDecimal(1, amount);
      preparedStatementUpdateTo.setString(2, toPaymentAccount);
      preparedStatementUpdateTo.executeUpdate();

      connection.commit();
      return true;
    } catch (SQLException e) {
      if (connection != null) {
        connection.rollback();
      }
      throw new DataBaseStatementException("Ошибка при переводе средств."
          + System.lineSeparator() + e.getMessage());
    } finally {
      if (preparedStatementUpdateFrom != null) {
        preparedStatementUpdateFrom.close();
      }
      if (preparedStatementUpdateTo != null) {
        preparedStatementUpdateTo.close();
      }
      if (connection != null) {
        connection.setAutoCommit(true);
        connection.close();
      }
    }
  }

  @Override
  public boolean replenish (String paymentAccount, BigDecimal amount) {
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BALANCE_SQL)) {

      preparedStatement.setBigDecimal(1, amount);
      preparedStatement.setString(2, paymentAccount);

      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException e) {
      throw new DataBaseStatementException("Ошибка при закрытии счета."
          + System.lineSeparator() + e.getMessage());
    }
  }

  @Override
  public Optional<Account> findById(Long id) {
    return Optional.empty();
  }

  @Override
  public List<Account> findAll() {
    return List.of();
  }

  @Override
  public List<Account> findOpenByClientId(Long clientId) {
    List<Account> accounts = new ArrayList<>();
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            FIND_ALL_OPEN_BY_CLIENT_ID)) {

      preparedStatement.setString(1, AccountStatus.OPEN.name());
      preparedStatement.setLong(2, clientId);

      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        accounts.add(createAccount(resultSet));
      }
    } catch (SQLException e) {
      throw new DataBaseStatementException("Ошибка при запросе всех открытых счетов клиента."
          + System.lineSeparator() + e.getMessage());
    }
    return accounts;
  }

  @Override
  public List<Account> findAllExceptCurrent(String paymentAccount) {
    List<Account> accounts = new ArrayList<>();
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            FIND_ALL_EXCEPT_CURRENT)) {

      preparedStatement.setString(1, paymentAccount);
      preparedStatement.setString(2, AccountStatus.OPEN.name());

      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        accounts.add(createAccount(resultSet));
      }
    } catch (SQLException e) {
      throw new DataBaseStatementException("Ошибка при запросе всех доступных для перевода счетов."
          + System.lineSeparator() + e.getMessage());
    }
    return accounts;
  }

  @Override
  public List<Currency> getCurrencies() {
    List<Currency> currencies = new ArrayList<>();
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_CURRENCY_SQL)) {

      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        currencies.add(createCurrency(resultSet));
      }
    } catch (SQLException e) {
      throw new DataBaseStatementException("Ошибка при запросе валют."
          + System.lineSeparator() + e.getMessage());
    }
    return currencies;
  }

  private Account createAccount(ResultSet resultSet) throws SQLException {
    return new Account(
        resultSet.getLong("account_id"),
        resultSet.getString("payment_account"),
        resultSet.getBigDecimal("balance"),
        AccountStatus.valueOf(resultSet.getString("status")),
        resultSet.getString("bic"),
        resultSet.getString("currency"),
        resultSet.getLong("client_id")
    );
  }

  private Currency createCurrency(ResultSet resultSet) throws SQLException {
    return new Currency(
        resultSet.getLong("currency_id"),
        resultSet.getString("currency"),
        resultSet.getString("label"),
        resultSet.getBoolean("disabled")
    );
  }
}

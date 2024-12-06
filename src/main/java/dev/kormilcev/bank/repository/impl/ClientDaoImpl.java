package dev.kormilcev.bank.repository.impl;

import dev.kormilcev.bank.db.ConnectionManager;
import dev.kormilcev.bank.db.ConnectionManagerImpl;
import dev.kormilcev.bank.exception.DataBaseStatementException;
import dev.kormilcev.bank.model.Client;
import dev.kormilcev.bank.repository.ClientDao;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.IOUtils;

public class ClientDaoImpl implements ClientDao {

  private static final String SAVE_SQL = """
      INSERT INTO client (surname, name, patronymic, phone, inn, address)
      VALUES (?, ?, ?, ?, ?, ?);
      """;

  private static final String UPDATE_SQL = """
      UPDATE client
      SET surname = ?,
          name = ?,
          patronymic = ?,
          phone = ?,
          inn = ?,
          address = ?
      WHERE client_id = ?;
      """;

  private static final String FIND_ALL_SQL = """
      SELECT client_id, surname, name, patronymic, phone, inn, address  FROM client;
      """;

  private static final String FIND_ALL_OPEN_SQL = """
      SELECT c.client_id, surname, name, patronymic, phone, inn, address  FROM client c
      JOIN account a ON c.client_id = a.client_id
      WHERE status = ?
      GROUP BY c.client_id;
      """;

  private static final String UPLOAD_SQL = """
      UPDATE client
      SET passport_scan = ?
      WHERE client_id = ?;
      """;
  private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
  private static ClientDao instance;

  public static synchronized ClientDao getInstance() {
    if (instance == null) {
      instance = new ClientDaoImpl();
    }
    return instance;
  }

  @Override
  public Client create(Client client) {
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

      preparedStatement.setString(1, client.getSurname());
      preparedStatement.setString(2, client.getName());
      preparedStatement.setString(3, client.getPatronymic());
      preparedStatement.setString(4, client.getPhone());
      preparedStatement.setString(5, client.getInn());
      preparedStatement.setString(6, client.getAddress());
      preparedStatement.executeUpdate();

      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet.next()) {
        client = new Client(
            resultSet.getLong("client_id"),
            client.getSurname(),
            client.getName(),
            client.getPatronymic(),
            client.getPhone(),
            client.getInn(),
            client.getAddress()
        );
      }
    } catch (SQLException e) {
      throw new DataBaseStatementException("Ошибка при сохранении клиента в базе данных."
          + System.lineSeparator() + e.getMessage());
    }
    return client;
  }

  @Override
  public Client update(Client client) {
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

      preparedStatement.setString(1, client.getSurname());
      preparedStatement.setString(2, client.getName());
      preparedStatement.setString(3, client.getPatronymic());
      preparedStatement.setString(4, client.getPhone());
      preparedStatement.setString(5, client.getInn());
      preparedStatement.setString(6, client.getAddress());
      preparedStatement.setLong(7, client.getClientId());
      preparedStatement.executeUpdate();

      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet.next()) {
        client = createClient(resultSet);
      }
    } catch (SQLException e) {
      throw new DataBaseStatementException("Ошибка при изменении клиента в базе данных."
          + System.lineSeparator() + e.getMessage());
    }
    return client;
  }

  @Override
  public Optional<Client> findById(Long id) {
    return Optional.empty();
  }

  @Override
  public List<Client> findAll() {
    List<Client> clients = new ArrayList<>();
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        clients.add(createClient(resultSet));
      }
    } catch (SQLException e) {
      throw new DataBaseStatementException("Ошибка при запросе всех клиентов."
          + System.lineSeparator() + e.getMessage());
    }
    return clients;
  }

  @Override
  public List<Client> findAllWithStatusOpen() {
    List<Client> clients = new ArrayList<>();
    try (Connection connection = connectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_OPEN_SQL)) {

      preparedStatement.setString(1, "OPEN");

      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        clients.add(createClient(resultSet));
      }
    } catch (SQLException e) {
      throw new DataBaseStatementException("Ошибка при запросе всех клиентов с открытыми счетами."
          + System.lineSeparator() + e.getMessage());
    }
    return clients;
  }

  @Override
  public boolean uploadFile(InputStream inputStream, Long client_id) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = connectionManager.getConnection();
      connection.setAutoCommit(false);

      byte[] bytes = IOUtils.toByteArray(inputStream);

      preparedStatement = connection.prepareStatement(UPLOAD_SQL);

      preparedStatement.setBytes(1, bytes);
      preparedStatement.setLong(2, client_id);

      preparedStatement.executeUpdate();

      connection.commit();
      return true;
    } catch (SQLException | IOException e) {
      if (connection != null) {
        connection.rollback();
      }
      throw new DataBaseStatementException("Ошибка при загрузке файла."
          + System.lineSeparator() + e.getMessage());
    } finally {
      if (preparedStatement != null) {
        preparedStatement.close();
      }
      if (connection != null) {
        connection.setAutoCommit(true);
        connection.close();
      }
    }
  }


  private Client createClient(ResultSet resultSet) throws SQLException {
    return new Client(
        resultSet.getLong("client_id"),
        resultSet.getString("surname"),
        resultSet.getString("name"),
        resultSet.getString("patronymic"),
        resultSet.getString("phone"),
        resultSet.getString("inn"),
        resultSet.getString("address")
    );
  }
}

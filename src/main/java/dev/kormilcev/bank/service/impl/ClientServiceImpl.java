package dev.kormilcev.bank.service.impl;


import dev.kormilcev.bank.exception.DataBaseStatementException;
import dev.kormilcev.bank.model.dto.ClientResponse;
import dev.kormilcev.bank.model.dto.NewClientRequest;
import dev.kormilcev.bank.model.dto.UpdateClientRequest;
import dev.kormilcev.bank.repository.ClientDao;
import dev.kormilcev.bank.repository.impl.ClientDaoImpl;
import dev.kormilcev.bank.service.ClientService;
import dev.kormilcev.bank.service.mapper.ClientMapper;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ClientServiceImpl implements ClientService {

  private final ClientDao clientDao = ClientDaoImpl.getInstance();
  private static final ClientMapper clientMapper = ClientMapper.INSTANCE;

  private static ClientService instance;

  public static  synchronized ClientService getInstance() {
    if (instance == null) {
      instance = new ClientServiceImpl();
    }
    return instance;
  }

  @Override
  public ClientResponse createClient(NewClientRequest request) {
    return clientMapper.clientToClientResponse(
        clientDao.create(clientMapper.newClientRequestToClient(request)));
  }

  @Override
  public Optional<ClientResponse> updateClient(UpdateClientRequest request) {
    return Optional.ofNullable(clientMapper.clientToClientResponse(
        clientDao.update(clientMapper.updateClientRequestToClient(request))));
  }

  @Override
  public List<ClientResponse> getAllClients() {
    return clientMapper.clientListToClientResponseList(clientDao.findAll());
  }

  @Override
  public List<ClientResponse> getClientsWithStatusOpen() {
    return clientMapper.clientListToClientResponseList(clientDao.findAllWithStatusOpen());
  }

  @Override
  public boolean uploadFile(InputStream inputStream, Long client_id) {
    try {
      return clientDao.uploadFile(inputStream, client_id);
    } catch (SQLException e) {
      throw new DataBaseStatementException("Ошибка при загрузке файла на уровне сервиса."
          + System.lineSeparator() + e.getMessage());
    }
  }
}

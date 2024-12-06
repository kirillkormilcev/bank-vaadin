package dev.kormilcev.bank.service;

import dev.kormilcev.bank.model.dto.ClientResponse;
import dev.kormilcev.bank.model.dto.NewClientRequest;
import dev.kormilcev.bank.model.dto.UpdateClientRequest;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface ClientService {

  ClientResponse createClient(NewClientRequest request);

  Optional<ClientResponse> updateClient(UpdateClientRequest request);

  List<ClientResponse> getAllClients();

  List<ClientResponse> getClientsWithStatusOpen();

  boolean uploadFile(InputStream inputStream, Long client_id);
}

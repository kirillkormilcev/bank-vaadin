package dev.kormilcev.bank.service.mapper;

import dev.kormilcev.bank.model.Client;
import dev.kormilcev.bank.model.dto.ClientResponse;
import dev.kormilcev.bank.model.dto.NewClientRequest;
import dev.kormilcev.bank.model.dto.UpdateClientRequest;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMapper {

  ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

  @Mapping(target = "clientId", ignore = true)
  Client newClientRequestToClient(NewClientRequest request);

  Client updateClientRequestToClient(UpdateClientRequest request);

  ClientResponse clientToClientResponse(Client client);

  List<ClientResponse> clientListToClientResponseList(List<Client> clients);
}

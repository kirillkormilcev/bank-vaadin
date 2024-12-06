package dev.kormilcev.bank.repository;

import dev.kormilcev.bank.model.Client;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public interface ClientDao extends Dao<Client, Long> {

  List<Client> findAllWithStatusOpen();

  boolean uploadFile(InputStream inputStream, Long client_id) throws SQLException;
}

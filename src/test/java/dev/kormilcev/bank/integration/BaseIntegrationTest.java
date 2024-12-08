package dev.kormilcev.bank.integration;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class BaseIntegrationTest {

  @Container
  static PostgreSQLContainer<?> container = new PostgreSQLContainer("postgres:14.4-alpine")
      .withDatabaseName("test_db")
      .withUsername("test")
      .withPassword("test");
}

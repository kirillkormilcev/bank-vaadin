package dev.kormilcev.bank.exception;

public class DataBaseStatementException extends RuntimeException {

  public DataBaseStatementException(String message) {
    super(message);
  }
}
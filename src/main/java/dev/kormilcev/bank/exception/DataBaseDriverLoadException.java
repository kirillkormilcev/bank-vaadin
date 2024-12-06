package dev.kormilcev.bank.exception;

public class DataBaseDriverLoadException extends RuntimeException {

  public DataBaseDriverLoadException(String message) {
    super(message);
  }
}
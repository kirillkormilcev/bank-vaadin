package dev.kormilcev.bank.validation;

import dev.kormilcev.bank.exception.ValidationException;
import java.math.BigDecimal;

public class AccountValidator {

  public static void throwIfTransferNotValid(BigDecimal balance, BigDecimal amount) {
    if (balance.compareTo(amount) < 0) {
      throw new ValidationException("Сумма перевода не должна быть больше остатка на счете.");
    }
    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new ValidationException("Сумма перевода не должна быть отрицательной.");
    }
    if (amount.compareTo(BigDecimal.ZERO) == 0) {
      throw new ValidationException("Сумма перевода не должна быть нулевой.");
    }
  }
}

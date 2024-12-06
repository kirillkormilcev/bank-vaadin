package dev.kormilcev.bank.model.dto;

import dev.kormilcev.bank.model.AccountStatus;
import java.io.Serializable;
import java.math.BigDecimal;

public record AccountResponse(
    String paymentAccount,
    BigDecimal balance,
    AccountStatus status,
    String bic,
    String currency,
    Long clientId) implements Serializable {
}

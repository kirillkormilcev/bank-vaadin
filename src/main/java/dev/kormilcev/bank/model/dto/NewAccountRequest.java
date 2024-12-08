package dev.kormilcev.bank.model.dto;

import lombok.Builder;

@Builder
public record NewAccountRequest(
    String bic,
    String currency,
    Long clientId) {

}

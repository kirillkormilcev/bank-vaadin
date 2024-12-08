package dev.kormilcev.bank.model.dto;

import lombok.Builder;

@Builder
public record UpdateAccountRequest(
    String paymentAccount,
    String bic) {

}

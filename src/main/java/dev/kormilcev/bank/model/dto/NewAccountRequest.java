package dev.kormilcev.bank.model.dto;

public record NewAccountRequest(
    String paymentAccount,
    String bic,
    String currency,
    Long clientId){
}

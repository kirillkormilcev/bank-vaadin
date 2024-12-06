package dev.kormilcev.bank.model.dto;

public record UpdateAccountRequest(
    String paymentAccount,
    String bic,
    Long clientId){
}

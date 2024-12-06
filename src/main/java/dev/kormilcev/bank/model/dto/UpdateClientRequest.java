package dev.kormilcev.bank.model.dto;

public record UpdateClientRequest(
    Long clientId,
    String surname,
    String name,
    String patronymic,
    String phone,
    String inn,
    String address){
}

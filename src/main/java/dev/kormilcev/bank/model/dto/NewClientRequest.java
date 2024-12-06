package dev.kormilcev.bank.model.dto;

public record NewClientRequest(
    String surname,
    String name,
    String patronymic,
    String phone,
    String inn,
    String address) {

}

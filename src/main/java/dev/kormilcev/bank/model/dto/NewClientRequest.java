package dev.kormilcev.bank.model.dto;

import lombok.Builder;

@Builder
public record NewClientRequest(
    String surname,
    String name,
    String patronymic,
    String phone,
    String inn,
    String address) {

}

package dev.kormilcev.bank.util;

import dev.kormilcev.bank.model.dto.NewClientRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestDataFactory {

  public NewClientRequest newClientRequest1 = NewClientRequest.builder()
      .surname("surname1")
      .name("name1")
      .patronymic("patronymic1")
      .phone("phone1")
      .inn("inn1")
      .address("address1")
      .build();

  public NewClientRequest newClientRequest2 = NewClientRequest.builder()
      .surname("surname2")
      .name("name2")
      .patronymic("patronymic2")
      .phone("phone2")
      .inn("inn2")
      .address("address2")
      .build();
}

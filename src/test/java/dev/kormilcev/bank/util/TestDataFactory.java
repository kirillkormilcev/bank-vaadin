package dev.kormilcev.bank.util;

import dev.kormilcev.bank.model.dto.NewAccountRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestDataFactory {

  public NewAccountRequest request = NewAccountRequest.builder()
      .bic("123456789")
      .currency("RUB")
      .clientId(1L)
      .build();
}

package dev.kormilcev.bank.model;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Builder
@EqualsAndHashCode
public class Account {

  Long accountId;
  String paymentAccount;
  BigDecimal balance;
  AccountStatus status;
  String bic;
  String currency;
  Long clientId;
}

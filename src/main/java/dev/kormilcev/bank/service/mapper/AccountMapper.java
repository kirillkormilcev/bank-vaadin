package dev.kormilcev.bank.service.mapper;

import dev.kormilcev.bank.model.Account;
import dev.kormilcev.bank.model.dto.AccountResponse;
import dev.kormilcev.bank.model.dto.NewAccountRequest;
import dev.kormilcev.bank.model.dto.UpdateAccountRequest;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

  AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

  @Mapping(target = "accountId", ignore = true)
  Account newAccountRequestToClient(NewAccountRequest request);

  @Mapping(target = "accountId", ignore = true)
  @Mapping(target = "currency", ignore = true)
  Account updateAccountRequestToClient(UpdateAccountRequest request);

  AccountResponse accountToAccountResponse(Account account);

  List<AccountResponse> accountListToAccountResponseList(List<Account> accounts);
}

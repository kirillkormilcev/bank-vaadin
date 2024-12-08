package dev.kormilcev.bank.util;

import com.vaadin.flow.component.UI;
import dev.kormilcev.bank.model.dto.AccountResponse;

public class NpeUtil {
  public static boolean returnIfNull(Object o, String route) {
    if (o == null) {
      UI.getCurrent().navigate(route);
    }
    return true;
  }

  public static String routeIfNullOrGetPaymentAccount(AccountResponse account, String route) {
    if (account == null) {
      UI.getCurrent().navigate(route);
    } else {
      return account.paymentAccount();
    }
    return null;
  }
}

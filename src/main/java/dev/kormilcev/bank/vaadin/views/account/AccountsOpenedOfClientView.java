package dev.kormilcev.bank.vaadin.views.account;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.kormilcev.bank.model.dto.AccountResponse;
import dev.kormilcev.bank.service.AccountService;
import dev.kormilcev.bank.service.impl.AccountServiceImpl;
import dev.kormilcev.bank.vaadin.views.client.ClientsWithOpenAccountsView;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@PageTitle("Открытые счета")
@Route("account-opened")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AccountsOpenedOfClientView extends Div {

  AccountService accountService;

  public AccountsOpenedOfClientView() {
    accountService = AccountServiceImpl.getInstance();

    VerticalLayout layout = new VerticalLayout();

    Long clientId = ComponentUtil.getData(UI.getCurrent(), Long.class);

    List<AccountResponse> accounts = new ArrayList<>();

    if (clientId == null) {
      UI.getCurrent().navigate(ClientsWithOpenAccountsView.class);
    } else {
      accounts = accountService.getAllOpenAccountsByClientId(clientId);
    }

    if (accounts == null || accounts.isEmpty()) {
      layout.add(new Text("Открытых счетов не найдено."));
    } else {
      accounts.forEach(account -> {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        horizontalLayout.add(new Text(account.paymentAccount()));
        horizontalLayout.add(new Tab());
        horizontalLayout.add(new Text(account.currency()));
        horizontalLayout.add(new Tab());
        horizontalLayout.add(new Text(account.balance().toPlainString()));
        horizontalLayout.add(new Tab());

        Button closeButton = new Button("Закрыть счет");
        closeButton.addClickListener(event -> {

          if (accountService.closeAccount(account.paymentAccount())) {
            Notification notification = Notification.show("Счет клиента закрыт");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
          } else {
            Notification notification = Notification.show("Счет клиента не удалось закрыть");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
          }
          UI.getCurrent().navigate(ClientsWithOpenAccountsView.class);
        });

        horizontalLayout.add(closeButton);

        horizontalLayout.add(createButtonAndRoute(
            "Перевести деньги со счета", "account-transfer", account
        ));

        horizontalLayout.add(createButtonAndRoute(
            "Пополнить счет", "account-replenish", account
        ));

        horizontalLayout.add(createButtonAndRoute(
            "Изменить счет", "account-update", account));

        layout.add(horizontalLayout);
      });
    }
    this.add(layout);
  }

  private Button createButtonAndRoute(String name, String route, AccountResponse account) {
    Button button = new Button(name);
    button.addClickListener(e -> {

      ComponentUtil.setData(UI.getCurrent(), AccountResponse.class, account);

      UI.getCurrent().navigate(route);

    });
    return button;
  }
}

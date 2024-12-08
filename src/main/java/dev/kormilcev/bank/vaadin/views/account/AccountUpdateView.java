package dev.kormilcev.bank.vaadin.views.account;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import dev.kormilcev.bank.model.dto.AccountResponse;
import dev.kormilcev.bank.model.dto.UpdateAccountRequest;
import dev.kormilcev.bank.service.AccountService;
import dev.kormilcev.bank.service.impl.AccountServiceImpl;
import dev.kormilcev.bank.vaadin.views.client.ClientsWithOpenAccountsView;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@PageTitle("Изменение счета")
@Route("account-update")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AccountUpdateView extends Composite<VerticalLayout> {

  AccountService accountService;

  public AccountUpdateView() {
    accountService = AccountServiceImpl.getInstance();

    VerticalLayout layoutColumn2 = new VerticalLayout();
    H3 h3 = new H3();
    FormLayout formLayout2Col = new FormLayout();

    TextField paymentAccountField = new TextField();
    TextField bicField = new TextField();
    TextField currencyField = new TextField();
    BigDecimalField balanceField = new BigDecimalField();

    HorizontalLayout layoutRow = new HorizontalLayout();

    Button updateButton = new Button();
    Button cancelButton = new Button();

    getContent().setWidth("100%");
    getContent().getStyle().set("flex-grow", "1");
    getContent().setJustifyContentMode(JustifyContentMode.START);
    getContent().setAlignItems(Alignment.CENTER);

    layoutColumn2.setWidth("100%");
    layoutColumn2.setMaxWidth("800px");
    layoutColumn2.setHeight("min-content");

    h3.setText("Данные счета");
    h3.setWidth("100%");

    formLayout2Col.setWidth("100%");

    paymentAccountField.setLabel("Номер счета");
    bicField.setLabel("БИК");
    currencyField.setLabel("Валюта");
    balanceField.setLabel("Сумма");

    currencyField.setWidth("min-content");

    layoutRow.addClassName(Gap.MEDIUM);
    layoutRow.setWidth("100%");
    layoutRow.getStyle().set("flex-grow", "1");

    updateButton.setText("Сохранить");
    updateButton.setWidth("min-content");
    updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    AccountResponse account = ComponentUtil.getData(UI.getCurrent(), AccountResponse.class);

    if (account != null) {
      paymentAccountField.setValue(account.paymentAccount());
      paymentAccountField.setReadOnly(true);
      bicField.setValue(account.bic());
      currencyField.setValue(account.currency());
      currencyField.setReadOnly(true);
      balanceField.setValue(account.balance());
      balanceField.setReadOnly(true);

      updateButton.addClickListener(e -> {
        Optional<AccountResponse> accountResponse = accountService.update(
            new UpdateAccountRequest(
                account.paymentAccount(),
                bicField.getValue()
            )
        );
        if (accountResponse.isPresent()) {
          Notification notification = Notification.show("Счет клиента изменен");
          notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } else {
          Notification notification = Notification.show("Не удалось изменить счет");
          notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        UI.getCurrent().navigate(ClientsWithOpenAccountsView.class);
      });
    } else {
      UI.getCurrent().navigate(ClientsWithOpenAccountsView.class);
    }

    cancelButton.setText("Отмена");
    cancelButton.setWidth("min-content");

    getContent().add(layoutColumn2);

    layoutColumn2.add(h3);
    layoutColumn2.add(formLayout2Col);

    formLayout2Col.add(paymentAccountField);
    formLayout2Col.add(bicField);
    formLayout2Col.add(currencyField);
    formLayout2Col.add(balanceField);

    layoutColumn2.add(layoutRow);

    layoutRow.add(updateButton);
    layoutRow.add(cancelButton);
  }
}

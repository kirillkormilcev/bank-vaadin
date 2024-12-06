package dev.kormilcev.bank.vaadin.views.account;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.kormilcev.bank.model.dto.AccountResponse;
import dev.kormilcev.bank.service.AccountService;
import dev.kormilcev.bank.service.impl.AccountServiceImpl;
import dev.kormilcev.bank.vaadin.views.client.ClientsWithOpenAccountsView;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Пополнение счета")
@Route("account-replenish")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AccountReplenishView extends Composite<VerticalLayout> {

  AccountService accountService;

  public AccountReplenishView() {
    accountService = AccountServiceImpl.getInstance();

    VerticalLayout layoutColumn2 = new VerticalLayout();

    H3 h3 = new H3();

    FormLayout formLayout2Col = new FormLayout();

    TextField accountField = new TextField();
    BigDecimalField amountField = new BigDecimalField();

    HorizontalLayout layoutRow = new HorizontalLayout();

    Button replenishButton = new Button();
    Button cancelButton = new Button();

    getContent().setWidth("100%");
    getContent().getStyle().set("flex-grow", "1");
    getContent().setJustifyContentMode(JustifyContentMode.START);
    getContent().setAlignItems(Alignment.CENTER);

    layoutColumn2.setWidth("100%");
    layoutColumn2.setMaxWidth("800px");
    layoutColumn2.setHeight("min-content");

    h3.setText("Выберите счет для перевода");
    h3.setWidth("100%");

    formLayout2Col.setWidth("100%");

    accountField.setLabel("Вы пополняете счет");

    AccountResponse account = ComponentUtil.getData(UI.getCurrent(), AccountResponse.class);

    if (account != null) {
      accountField.setValue(account.paymentAccount());
      accountField.setReadOnly(true);

    } else {
      UI.getCurrent().navigate(ClientsWithOpenAccountsView.class);
    }

    amountField.setLabel("Сумма");
    amountField.setWidth("min-content");

    layoutRow.addClassName(Gap.MEDIUM);
    layoutRow.setWidth("100%");
    layoutRow.getStyle().set("flex-grow", "1");

    replenishButton.setText("Пополнить");
    replenishButton.setWidth("min-content");
    replenishButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    replenishButton.addClickListener(e -> {

      if (accountService.replenish(account.paymentAccount(), amountField.getValue())) {
        Notification notification = Notification.show("Пополнение выполнено");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      } else {
        Notification notification = Notification.show("Не удалось совершить пополнение");
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
      }
      ComponentUtil.setData(UI.getCurrent(), Long.class, account.clientId());
      UI.getCurrent().navigate(AccountsOpenedOfClientView.class);
    });

    cancelButton.setText("Отмена");
    cancelButton.setWidth("min-content");

    getContent().add(layoutColumn2);

    layoutColumn2.add(h3);
    layoutColumn2.add(formLayout2Col);

    formLayout2Col.add(accountField);
    formLayout2Col.add(amountField);

    layoutColumn2.add(layoutRow);

    layoutRow.add(replenishButton);
    layoutRow.add(cancelButton);
  }
}

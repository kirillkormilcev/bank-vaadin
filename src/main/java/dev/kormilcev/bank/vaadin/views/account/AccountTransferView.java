package dev.kormilcev.bank.vaadin.views.account;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import dev.kormilcev.bank.service.AccountService;
import dev.kormilcev.bank.service.impl.AccountServiceImpl;
import dev.kormilcev.bank.vaadin.views.client.ClientsWithOpenAccountsView;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@PageTitle("Перевод денег на счет другого клиента")
@Route("account-transfer")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AccountTransferView extends Composite<VerticalLayout> {

  AccountService accountService;

  public AccountTransferView () {
    accountService = AccountServiceImpl.getInstance();

    VerticalLayout layoutColumn2 = new VerticalLayout();
    H3 h3 = new H3();
    FormLayout formLayout2Col = new FormLayout();

    TextField fromAccountField = new TextField();
    BigDecimalField amountField = new BigDecimalField();
    ComboBox toAccountComboBox = new ComboBox();

    HorizontalLayout layoutRow = new HorizontalLayout();
    Button transferButton = new Button();
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

    fromAccountField.setLabel("Вы переводите со счета");

    AccountResponse account = ComponentUtil.getData(UI.getCurrent(), AccountResponse.class);

    if (account != null) {
      fromAccountField.setValue(account.paymentAccount());
      fromAccountField.setReadOnly(true);

    } else {
      UI.getCurrent().navigate(ClientsWithOpenAccountsView.class);
    }

    amountField.setLabel("Сумма");
    amountField.setWidth("min-content");

    toAccountComboBox.setLabel("На счет");
    toAccountComboBox.setWidth("min-content");

    setComboBoxAvailableAccountsData(toAccountComboBox, account);

    layoutRow.addClassName(Gap.MEDIUM);
    layoutRow.setWidth("100%");
    layoutRow.getStyle().set("flex-grow", "1");

    transferButton.setText("Перевести");
    transferButton.setWidth("min-content");
    transferButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    transferButton.addClickListener(e -> {

      if (accountService.transfer(account.paymentAccount(), account.balance(),
          amountField.getValue(), ((AccountResponse) toAccountComboBox.getValue()).paymentAccount())) {
        Notification notification = Notification.show("Перевод выполнен");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      } else {
        Notification notification = Notification.show("Не удалось совершить перевод");
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

    formLayout2Col.add(fromAccountField);
    formLayout2Col.add(amountField);
    formLayout2Col.add(toAccountComboBox);

    layoutColumn2.add(layoutRow);

    layoutRow.add(transferButton);
    layoutRow.add(cancelButton);
  }

  private void setComboBoxAvailableAccountsData(ComboBox comboBox, AccountResponse account) {
    List<AccountResponse> accounts = accountService.getAllAvailableAccounts(account.paymentAccount());

    comboBox.setItems(accounts);
    comboBox.setItemLabelGenerator(item -> ((AccountResponse) item).paymentAccount());
  }
}

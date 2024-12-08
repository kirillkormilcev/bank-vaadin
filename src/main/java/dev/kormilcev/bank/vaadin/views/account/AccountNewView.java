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
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import dev.kormilcev.bank.model.Currency;
import dev.kormilcev.bank.model.dto.AccountResponse;
import dev.kormilcev.bank.model.dto.NewAccountRequest;
import dev.kormilcev.bank.service.AccountService;
import dev.kormilcev.bank.service.impl.AccountServiceImpl;
import dev.kormilcev.bank.vaadin.views.client.ClientsView;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@PageTitle("Создание счета")
@Route("account-new")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AccountNewView extends Composite<VerticalLayout> {

  AccountService accountService;

  public AccountNewView() {
    accountService = AccountServiceImpl.getInstance();

    VerticalLayout layoutColumn2 = new VerticalLayout();
    H3 h3 = new H3();
    FormLayout formLayout2Col = new FormLayout();

    TextField paymentAccountField = new TextField();
    TextField bicField = new TextField();
    Select currencySelect = new Select();

    HorizontalLayout layoutRow = new HorizontalLayout();

    Button createButton = new Button();
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
    paymentAccountField.setValue("будет сгенерирован");
    paymentAccountField.setReadOnly(true);

    bicField.setLabel("БИК");
    bicField.setValue("044525999");
    bicField.setReadOnly(true);

    currencySelect.setLabel("Валюта");

    currencySelect.setWidth("min-content");

    setSelectCurrencyData(currencySelect);

    layoutRow.addClassName(Gap.MEDIUM);
    layoutRow.setWidth("100%");
    layoutRow.getStyle().set("flex-grow", "1");

    createButton.setText("Создать");
    createButton.setWidth("min-content");
    createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    Long clientId = ComponentUtil.getData(UI.getCurrent(), Long.class);

    createButton.addClickListener(e -> {
      AccountResponse accountResponse = accountService.create(
          new NewAccountRequest(
              bicField.getValue(),
              ((Currency) currencySelect.getValue()).getCurrency(),
              clientId
          )
      );
      if (accountResponse != null) {
        Notification notification = Notification.show("Счет клиента создан");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      } else {
        Notification notification = Notification.show("Не удалось создать счет");
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
      }
      UI.getCurrent().navigate(ClientsView.class);
    });

    cancelButton.setText("Отмена");
    cancelButton.setWidth("min-content");

    getContent().add(layoutColumn2);

    layoutColumn2.add(h3);
    layoutColumn2.add(formLayout2Col);

    formLayout2Col.add(paymentAccountField);
    formLayout2Col.add(bicField);
    formLayout2Col.add(currencySelect);

    layoutColumn2.add(layoutRow);

    layoutRow.add(createButton);
    layoutRow.add(cancelButton);
  }

  private void setSelectCurrencyData(Select select) {
    List<Currency> currencies = accountService.getCurrencies();

    select.setItems(currencies);
    select.setItemLabelGenerator(item -> ((Currency) item).getLabel());
    select.setItemEnabledProvider(item -> !Boolean.TRUE.equals(((Currency) item).getDisabled()));
  }

}

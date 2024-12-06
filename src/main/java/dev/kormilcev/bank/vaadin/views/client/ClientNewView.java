package dev.kormilcev.bank.vaadin.views.client;

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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import dev.kormilcev.bank.model.dto.NewClientRequest;
import dev.kormilcev.bank.service.ClientService;
import dev.kormilcev.bank.service.impl.ClientServiceImpl;
import dev.kormilcev.bank.vaadin.views.mian.MainView;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Новый клиент")
@Route("client-new")
@Menu(order = 1, icon = LineAwesomeIconUrl.USER)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClientNewView extends Composite<VerticalLayout> {

    ClientService clientService;

    public ClientNewView() {
        clientService = ClientServiceImpl.getInstance();

        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();

        TextField surnameField = new TextField();
        TextField nameField = new TextField();
        TextField patronymicField = new TextField();
        TextField phoneField = new TextField();
        TextField innField = new TextField();
        TextField addressField = new TextField();

        HorizontalLayout layoutRow = new HorizontalLayout();

        Button saveButton = new Button();
        Button cancelButton = new Button();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);

        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");

        h3.setText("Данные клиента");
        h3.setWidth("100%");

        formLayout2Col.setWidth("100%");

        surnameField.setLabel("Фамилия");
        nameField.setLabel("Имя");
        patronymicField.setLabel("Отчество");
        phoneField.setLabel("Телефон");
        phoneField.setWidth("min-content");
        innField.setLabel("ИНН");
        addressField.setLabel("Адрес");

        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");

        saveButton.setText("Сохранить");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        saveButton.addClickListener(e -> {
            clientService.createClient(
                new NewClientRequest(
                    surnameField.getValue(),
                    nameField.getValue(),
                    patronymicField.getValue(),
                    phoneField.getValue(),
                    innField.getValue(),
                    addressField.getValue()
                )
            );

            Notification notification = Notification.show("Клиент создан");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            UI.getCurrent().navigate(MainView.class);
        });

        cancelButton.setText("Отмена");
        cancelButton.setWidth("min-content");

        getContent().add(layoutColumn2);

        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);

        formLayout2Col.add(surnameField);
        formLayout2Col.add(nameField);
        formLayout2Col.add(patronymicField);
        formLayout2Col.add(phoneField);
        formLayout2Col.add(innField);
        formLayout2Col.add(addressField);

        layoutColumn2.add(layoutRow);

        layoutRow.add(saveButton);
        layoutRow.add(cancelButton);
    }
}

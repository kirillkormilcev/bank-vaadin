package dev.kormilcev.bank.vaadin.views.client;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import dev.kormilcev.bank.model.dto.ClientResponse;
import dev.kormilcev.bank.model.dto.UpdateClientRequest;
import dev.kormilcev.bank.service.ClientService;
import dev.kormilcev.bank.service.impl.ClientServiceImpl;
import java.io.InputStream;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@PageTitle("Изменить данные клиента")
@Route("client-update")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClientUpdateView extends Composite<VerticalLayout> {

  ClientService clientService;

  public ClientUpdateView() {
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

    MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    Upload passportUpload = new Upload(buffer);

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

    Button uploadButton = new Button("Загрузить копию паспорта");
    passportUpload.setUploadButton(uploadButton);
    passportUpload.setDropLabel(new Div("Переместите файл сюда"));

    ClientResponse client = ComponentUtil.getData(UI.getCurrent(), ClientResponse.class);

    if (client != null) {
      surnameField.setValue(client.surname());
      nameField.setValue(client.name());
      patronymicField.setValue(client.patronymic());
      phoneField.setValue(client.phone());
      innField.setValue(client.inn());
      addressField.setValue(client.address());
    } else {
      UI.getCurrent().navigate(ClientsView.class);
    }

    layoutRow.addClassName(Gap.MEDIUM);
    layoutRow.setWidth("100%");
    layoutRow.getStyle().set("flex-grow", "1");

    saveButton.setText("Сохранить");
    saveButton.setWidth("min-content");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    saveButton.addClickListener(e -> {
      Optional<ClientResponse> response = clientService.updateClient(
          new UpdateClientRequest(
              client.clientId(),
              surnameField.getValue(),
              nameField.getValue(),
              patronymicField.getValue(),
              phoneField.getValue(),
              innField.getValue(),
              addressField.getValue()
          )
      );
      if (response.isPresent()) {
        Notification notification = Notification.show("Клиент изменен");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      } else {
        Notification notification = Notification.show("Не удалось изменить клиента");
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
      }
      UI.getCurrent().navigate(ClientsView.class);
    });

    passportUpload.addSucceededListener(e -> {
      String filename = e.getFileName();
      InputStream inputStream = buffer.getInputStream(filename);
      boolean uploaded = clientService.uploadFile(inputStream, client.clientId());

      if (uploaded) {
        Notification notification = Notification.show("Файл загружен");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      } else {
        Notification notification = Notification.show("Не удалось загрузить файл");
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
      }
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
    formLayout2Col.add(passportUpload);

    layoutColumn2.add(layoutRow);

    layoutRow.add(saveButton);
    layoutRow.add(cancelButton);
  }
}

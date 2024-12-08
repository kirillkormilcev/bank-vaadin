package dev.kormilcev.bank.vaadin.handler;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.ErrorHandler;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Handler{

  static ErrorHandler instance;

  public static synchronized ErrorHandler getInstance() {
    if (instance == null) {
      instance = e -> UI.getCurrent().access(() -> {
        Notification notification = Notification.show(e.getThrowable().getMessage());
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
      });
    }
    return instance;
  }
}

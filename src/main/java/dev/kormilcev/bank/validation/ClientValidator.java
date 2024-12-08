package dev.kormilcev.bank.validation;

import com.vaadin.flow.component.map.configuration.View;

import com.vaadin.flow.component.textfield.TextField;
import dev.kormilcev.bank.exception.ValidationException;
import java.util.List;


public class ClientValidator extends View {

  public static boolean throwIfClientNotValid(List<TextField> fields){

    fields.forEach(field->{
      if (field.isInvalid()) {
        throw new ValidationException("Поле " + field.getLabel() + " не корректно.");
      }
    });

    return true;
  }
}

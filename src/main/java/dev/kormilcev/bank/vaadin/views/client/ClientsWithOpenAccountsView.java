package dev.kormilcev.bank.vaadin.views.client;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.kormilcev.bank.model.dto.ClientResponse;
import dev.kormilcev.bank.service.ClientService;
import dev.kormilcev.bank.service.impl.ClientServiceImpl;
import dev.kormilcev.bank.vaadin.views.account.AccountsOpenedOfClientView;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Клиенты с открытыми счетами")
@Route("clients-open")
@Menu(order = 3, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClientsWithOpenAccountsView extends Div {

  ClientService clientService;

  public ClientsWithOpenAccountsView() {
    clientService = ClientServiceImpl.getInstance();

    VerticalLayout layout = new VerticalLayout();

    List<ClientResponse> clients = clientService.getClientsWithStatusOpen();

    if (clients == null || clients.isEmpty()) {
      layout.add(new Text("Клиентов с открытыми счетами не найдено."));

    } else {
      clients.forEach(client -> {

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        horizontalLayout.add(new Text(client.name()));
        horizontalLayout.add(new Tab());
        horizontalLayout.add(new Text(client.surname()));
        horizontalLayout.add(new Tab());

        Button button = new Button("Выбрать счет для действий");
        button.addClickListener(e -> {

          ComponentUtil.setData(UI.getCurrent(), Long.class, client.clientId());

          UI.getCurrent().navigate(AccountsOpenedOfClientView.class);
        });

        horizontalLayout.add(button);

        layout.add(horizontalLayout);
      });
    }
    this.add(layout);
  }
}

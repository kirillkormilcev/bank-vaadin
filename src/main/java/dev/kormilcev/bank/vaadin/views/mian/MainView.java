package dev.kormilcev.bank.vaadin.views.mian;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.kormilcev.bank.vaadin.views.client.ClientNewView;
import dev.kormilcev.bank.vaadin.views.client.ClientsView;
import dev.kormilcev.bank.vaadin.views.client.ClientsWithOpenAccountsView;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Главная")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.BALANCE_SCALE_SOLID)
public class MainView extends Composite<VerticalLayout> {

  public MainView() {
    MenuBar menuBar = new MenuBar();
    getContent().setWidth("100%");
    getContent().getStyle().set("flex-grow", "1");
    getContent().setJustifyContentMode(JustifyContentMode.START);
    getContent().setAlignItems(Alignment.CENTER);
    menuBar.setWidth("min-content");
    setMenuBarSampleData(menuBar);
    getContent().add(menuBar);
  }

  private void setMenuBarSampleData(MenuBar menuBar) {

    menuBar.addItem("Главная", e -> {
      UI.getCurrent().navigate(MainView.class);
    });
    menuBar.addItem("Новый клиент", e -> {
      UI.getCurrent().navigate(ClientNewView.class);
    });
    menuBar.addItem("Все клиенты", e -> {
      UI.getCurrent().navigate(ClientsView.class);
    });
    menuBar.addItem("Клиенты с открытыми счетами", e -> {
      UI.getCurrent().navigate(ClientsWithOpenAccountsView.class);
    });
  }
}

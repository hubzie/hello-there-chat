package pl.hellothere.client.view.app;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import pl.hellothere.client.view.controller.ClientViewController;
import pl.hellothere.containers.socket.data.messages.TextMessage;

public class ClientViewAppController {
    @FXML VBox messagesBox;
    @FXML TextField newMessageField;
    @FXML ScrollPane messagesPane;
    public ScrollPane groupsPane;
    public VBox groupsBox;

    public ClientViewAppController() { ClientViewController.getAppView().setCvlc(this); }

    @FXML private void handleSendButtonAction() {
        if(!newMessageField.getText().equals("")) {
            ClientViewController.getAppView().getSendAction().accept(new TextMessage(newMessageField.getText()));
            newMessageField.clear();
            ClientViewController.getAppView().setScrollMessagesToBottom();
        }
    }

    @FXML private void handleLogoutButtonAction() {
        ClientViewController.getAppView().getLogoutAction().accept(null);
    }
}

package pl.hellothere.client.view.app;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import pl.hellothere.client.view.controller.ClientViewController;
import pl.hellothere.containers.socket.data.messages.Message;
import pl.hellothere.containers.socket.data.messages.TextMessage;

public class ClientViewAppController {
    @FXML VBox messagesBox;
    @FXML TextField newMessageField;
    @FXML ScrollPane messagesPane;
    @FXML ScrollPane groupsPane;
    @FXML VBox groupsBox;

    public ClientViewAppController() { ClientViewController.getAppView().setCvlc(this); }

    @FXML private void handleSendButtonAction() {
        if(!newMessageField.getText().equals("")) {
            ClientViewController.getAppView().getSendAction().accept(Message.createMessage(newMessageField.getText(), TextMessage.TYPE));
            newMessageField.clear();
            ClientViewController.getAppView().setScrollMessagesToBottom();
        }
    }

    @FXML private void handleLogoutButtonAction() { ClientViewController.getAppView().getLogoutAction().accept(null); }

    @FXML private void handleCloseButtonAction() { ClientViewController.getAppView().close(); }

    @FXML private void handleMinimizeButtonAction() { ClientViewController.getAppView().minimize(); }
}

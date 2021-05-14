package pl.hellothere.client.view.app;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.hellothere.client.view.controller.ClientViewController;
import pl.hellothere.containers.socket.data.messages.TextMessage;

public class ClientViewAppController {
    @FXML VBox messagesBox;
    @FXML TextField newMessageField;
    @FXML HBox appTop;
    @FXML ScrollPane messagesPane;

    public ClientViewAppController() { ClientViewController.getAppView().setCvlc(this); }

    @FXML private void handleSendButtonAction() {
        ClientViewController.getAppView().getSendAction().accept(new TextMessage(newMessageField.getText()));
        newMessageField.clear();
        Platform.runLater(() -> messagesPane.setVvalue(messagesPane.getVmax()));
    }

    @FXML private void handleLogoutButtonAction() {
        ClientViewController.getAppView().getLogoutAction().accept(null);
    }
}

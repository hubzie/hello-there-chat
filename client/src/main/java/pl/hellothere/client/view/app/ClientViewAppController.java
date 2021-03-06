package pl.hellothere.client.view.app;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import pl.hellothere.client.view.controller.ClientViewController;
import pl.hellothere.containers.socket.data.messages.Message;
import pl.hellothere.containers.socket.data.messages.MessageType;

import java.io.*;
import java.util.Properties;

public class ClientViewAppController {
    @FXML VBox messagesBox;
    @FXML TextField newMessageField;
    @FXML ScrollPane messagesPane;
    @FXML ScrollPane groupsPane;
    @FXML VBox groupsBox;
    private boolean isDarkMode = false;

    public ClientViewAppController() {
        ClientViewController.getAppView().setCvac(this);
    }

    public void ClearMessagesBox() { messagesBox.getChildren().clear(); }

    @FXML private void handleSendButtonAction() {
        if(!newMessageField.getText().equals("")) {
            ClientViewController.getAppView().getSendAction().accept(Message.createMessage(newMessageField.getText(), MessageType.Text));
            newMessageField.clear();
            ClientViewController.getAppView().setScrollMessagesToBottom();
        }
    }

    @FXML private void handleLogoutButtonAction() { ClientViewController.getAppView().getLogoutAction().accept(null); }

    @FXML private void handleCloseButtonAction() { ClientViewController.getAppView().close(); }

    @FXML private void handleMinimizeButtonAction() { ClientViewController.getAppView().minimize(); }

    public void handleStickerButtonAction() {
        if(!ClientViewController.getAppView().getStickerStage().isShowing()) ClientViewController.getAppView().showStickerSelect();
        else ClientViewController.getAppView().hideStickerSelect();
    }

    public void handleChangeStyleButtonAction() {
        ClientViewController.getAppView().getPrimaryStage().getScene().getRoot().getStylesheets().clear();
        ClientViewController.getAppView().getStickerStage().getScene().getRoot().getStylesheets().clear();

        if(!isDarkMode) {
            ClientViewController.getAppView().getPrimaryStage().getScene().getRoot().getStylesheets().add("css/AppStyleDarkGrey.css");
            ClientViewController.getAppView().getStickerStage().getScene().getRoot().setStyle("-fx-background-color: derive(Grey, -70.0%);");
            ((ScrollPane)ClientViewController.getAppView().getStickerStage().getScene().getRoot()).getContent().setStyle("-fx-background-color: derive(Grey, -70.0%);");
            ClientViewController.getAppView().getStickerStage().getScene().getRoot().getStylesheets().add("AppStyleDarkGrey.css");
            ClientViewController.getAppView().getStickerStage().getScene().getRoot().setStyle("-fx-border-width: 2; -fx-border-color: derive(Grey, -70.0%); -fx-background-color: derive(Grey, -70.0%); -fx-background: derive(Grey, -70.0%);");

            try {
                Properties prop = new Properties();
                prop.setProperty("style.mode", "dark");
                prop.store(new FileOutputStream("src/main/resources/style.properties"), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            ClientViewController.getAppView().getPrimaryStage().getScene().getRoot().getStylesheets().add("css/AppStyleGrey.css");
            ClientViewController.getAppView().getStickerStage().getScene().getRoot().setStyle("-fx-background-color: WhiteSmoke;");
            ((ScrollPane)ClientViewController.getAppView().getStickerStage().getScene().getRoot()).getContent().setStyle("-fx-background-color: WhiteSmoke;");
            ClientViewController.getAppView().getStickerStage().getScene().getRoot().getStylesheets().add("AppStyleGrey.css");
            ClientViewController.getAppView().getStickerStage().getScene().getRoot().setStyle("-fx-border-width: 2; -fx-border-color: WhiteSmoke; -fx-background-color: WhiteSmoke;");

            try {
                Properties prop = new Properties();
                prop.setProperty("style.mode", "light");
                prop.store(new FileOutputStream("src/main/resources/style.properties"), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        isDarkMode = !isDarkMode;
    }

    public void handleCreateConversationButtonAction() {
        try {
            ClientViewController.getGroupCreationView().run();
            if(ClientViewController.getGroupModificationView() != null) ClientViewController.getGroupModificationView().close();
        } catch (Exception e) {
            ClientViewController.showErrorMessage("View Error");
            e.printStackTrace();
        }
    }

    public void handleModifyConversationButtonAction() {
        if(ClientViewController.getAppView().getConversationDetails() == null || groupsBox.getChildren().size() == 0) return;
        try {
            ClientViewController.getGroupModificationView().run();
            if(ClientViewController.getGroupCreationView() != null) ClientViewController.getGroupCreationView().close();
        } catch (Exception e) {
            ClientViewController.showErrorMessage("View Error");
            e.printStackTrace();
        }
    }

    public void handleUserDataButtonAction() {
        try {
            ClientViewController.getUserModificationView().run();
        } catch (Exception e) {
            ClientViewController.showErrorMessage("View Error");
            e.printStackTrace();
        }
    }
}

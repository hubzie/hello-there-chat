package pl.hellothere.client.view.login;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import pl.hellothere.client.view.controller.ClientViewController;

public class ClientViewLoginController {
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private HBox resultPromptBox;

    public ClientViewLoginController() {
        ClientViewController.getLoginView().setCvlc(this);
    }

    HBox getResultPromptBox() {
        return resultPromptBox;
    }

    @FXML private void handleSignInButtonAction() {
        try {
            ClientViewController.getLoginView().getSignInAction().accept(loginField.getText(), passwordField.getText());
        } catch (Exception e) {
            ClientViewController.showErrorMessage("Already logged in");
        }
    }
}
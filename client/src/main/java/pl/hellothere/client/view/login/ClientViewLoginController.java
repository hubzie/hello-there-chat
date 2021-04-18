package pl.hellothere.client.view.login;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import pl.hellothere.client.view.controller.ClientViewController;

public class ClientViewLoginController {
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private HBox resultPromptBox;

    public ClientViewLoginController() {
        ClientViewController.getLoginView().setCvlc(this);
    }

    public HBox getResultPromptBox() {
        return resultPromptBox;
    }

    @FXML private void handleSignInButtonAction() {
        try {
            ClientViewController.getLoginView().getSignInAction().accept(loginField.getText(), passwordField.getText());
            if (!ClientViewController.getLoginView().getCredentialsTest().apply(loginField.getText(), passwordField.getText())) {
                //Reakcja na bledne dane logowania
                System.out.println("Wrong credentials");
                ClientViewController.getLoginView().loginFailMessage();
            }
            else {
                //Reakcja na poprawne dane logowania
                System.out.println("Correct credentials");
                getResultPromptBox().getChildren().clear();
                getResultPromptBox().getChildren().add(new Label() {
                    {
                        setText("Logged in");
                        setTextFill(Color.LIMEGREEN);
                    }
                });
            }
        } catch (Exception e) {
            ClientViewController.showErrorMessage("Already logged in");
        }
    }
}
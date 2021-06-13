package pl.hellothere.client.view.registration;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import pl.hellothere.client.view.controller.ClientViewController;

public class ClientViewRegistrationController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private HBox resultPromptBox;

    void setLoginField(String login) { loginField.setText(login); }

    void setPasswordField(String password) { passwordField.setText(password); }

    public ClientViewRegistrationController() { ClientViewController.getRegistrationView().setCvrc(this); }

    public void handleMinimizeButtonAction() { ClientViewController.getRegistrationView().minimize(); }

    public void addErrorPrompt(String message) {
        resultPromptBox.getChildren().add(new Label() {
            {
                setText(message);
                setTextFill(Color.RED);
            }
        });
    }

    public void clearResultPrompt() { resultPromptBox.getChildren().clear(); }

    public void handleCloseButtonAction() {
        try {
            ClientViewController.getLoginView().run();
        } catch (Exception e) {
            ClientViewController.showErrorMessage("Login Error");
            e.printStackTrace();
        }
        ClientViewController.getRegistrationView().close();
    }

    public void handleRegisterButtonAction() {
        resultPromptBox.getChildren().clear();

        if(nameField.getText().equals("")) {
            addErrorPrompt("Name cannot be empty");
            return;
        }

        if(emailField.getText().equals("")) {
            addErrorPrompt("E-mail cannot be empty");
            return;
        }

        if(loginField.getText().equals("")) {
            addErrorPrompt("Login cannot be empty");
            return;
        }

        if(passwordField.getText().equals("")) {
            addErrorPrompt("Password cannot be empty");
            return;
        }

        if(!confirmPasswordField.getText().equals(passwordField.getText())) {
            addErrorPrompt("Passwords doesn't match");
            return;
        }

        resultPromptBox.getChildren().add(new Label() {
            {
                setText("Registering...");
                setTextFill(Color.LIMEGREEN);
            }
        });

        ClientViewController.getRegistrationView().getRegisterAction().accept(nameField.getText(), emailField.getText(), loginField.getText(), passwordField.getText());
    }
}

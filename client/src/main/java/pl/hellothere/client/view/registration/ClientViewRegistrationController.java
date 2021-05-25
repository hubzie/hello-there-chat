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
            resultPromptBox.getChildren().add(new Label() {
                {
                    setText("Name cannot be empty");
                    setTextFill(Color.RED);
                }
            });
            return;
        }

        if(emailField.getText().equals("")) {
            resultPromptBox.getChildren().add(new Label() {
                {
                    setText("E-mail cannot be empty");
                    setTextFill(Color.RED);
                }
            });
            return;
        }

        if(loginField.getText().equals("")) {
            resultPromptBox.getChildren().add(new Label() {
                {
                    setText("Login cannot be empty");
                    setTextFill(Color.RED);
                }
            });
            return;
        }

        if(passwordField.getText().equals("")) {
            resultPromptBox.getChildren().add(new Label() {
                {
                    setText("Password cannot be empty");
                    setTextFill(Color.RED);
                }
            });
            return;
        }

        if(!confirmPasswordField.getText().equals(passwordField.getText())) {
            resultPromptBox.getChildren().add(new Label() {
                {
                    setText("Passwords doesn't match");
                    setTextFill(Color.RED);
                }
            });
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

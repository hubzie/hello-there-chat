package pl.hellothere.client.view.usermodification;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import pl.hellothere.client.view.controller.ClientViewController;
import pl.hellothere.containers.socket.authorization.ModifyUserResult;

public class ClientViewUserModificationController {
    public TextField nameField;
    public TextField loginField;
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;
    public HBox resultPromptBox;

    public void handleCloseButtonAction() {
        ClientViewController.getUserModificationView().close();
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
                setText("Connecting...");
                setTextFill(Color.LIMEGREEN);
            }
        });

        ModifyUserResult.Code resCode = ClientViewController.getAppView().getModifyUserAction()
                .apply( ClientViewController.getAppView().getCurUserID(), nameField.getText(), loginField.getText(), passwordField.getText() );

        if(resCode.equals(ModifyUserResult.Code.LOGIN_ALREADY_USED)) {
            resultPromptBox.getChildren().clear();
            resultPromptBox.getChildren().add(new Label() {
                {
                    setText("Login Already Used");
                    setTextFill(Color.RED);
                }
            });
        }

        if(resCode.equals(ModifyUserResult.Code.SERVER_ERROR)) {
            resultPromptBox.getChildren().clear();
            resultPromptBox.getChildren().add(new Label() {
                {
                    setText("Server Error");
                    setTextFill(Color.RED);
                }
            });
        }

        if(resCode.equals(ModifyUserResult.Code.OK)) {
            resultPromptBox.getChildren().clear();
            resultPromptBox.getChildren().add(new Label() {
                {
                    setText("Data Updated");
                    setTextFill(Color.LIMEGREEN);
                }
            });
            ClientViewController.getUserModificationView().close();
        }
    }
}

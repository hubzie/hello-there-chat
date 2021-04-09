package pl.hellothere.client.view.login;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ClientViewLogin extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(makeLoginScene());
        primaryStage.setTitle("Hello There");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Scene makeLoginScene() throws Exception {
        Scene loginScene = new Scene(makeLoginLayout(),300,220);

        return loginScene;
    }

    private GridPane makeLoginLayout() throws Exception {
        GridPane loginLayout = new GridPane();
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setHgap(10);
        loginLayout.setVgap(10);
        loginLayout.getStylesheets().add("LoginStyle.css");
        loginLayout.setStyle("-fx-background-color: AliceBlue;");

        Label hellothereLabel = new Label("Hello There");
        hellothereLabel.setFont(Font.font("Lucida Console", FontWeight.SEMI_BOLD, 30));
        HBox hbHellothereLabel = new HBox();
        hbHellothereLabel.setAlignment(Pos.CENTER);
        hbHellothereLabel.getChildren().add(hellothereLabel);

        Label loginLabel = new Label("Login:");
        loginLabel.setFont(Font.font("Lucida Console"));

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Lucida Console"));

        TextField loginField = new TextField();
        loginField.setPromptText("Enter Login");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");

        Button signInButton = new Button("Sign in");
        signInButton.setOnAction(makeConfirmButtonAction(loginField, passwordField, loginLayout));
        HBox hbConfirmButton = new HBox();
        hbConfirmButton.setAlignment(Pos.CENTER);
        hbConfirmButton.getChildren().add(signInButton);

        loginLayout.add(hbHellothereLabel, 0,0, 2, 1);
        loginLayout.addRow(2, loginLabel, loginField);
        loginLayout.addRow(3, passwordLabel, passwordField);
        loginLayout.add(hbConfirmButton,0,5, 2, 1);

        return loginLayout;
    }

    private EventHandler<ActionEvent> makeConfirmButtonAction(TextField loginField, PasswordField passwordField, GridPane loginLayout) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent aEvt){
                System.out.println("Login: " + loginField.getText() + " " + "Password: " + passwordField.getText());
                if (!loginField.getText().equals("jarjar")) {
                    Label wrongLoginOrPassword = new Label("Wrong login or password");
                    wrongLoginOrPassword.setTextFill(Color.RED);
                    HBox hbWrongLoginOrPassword = new HBox();
                    hbWrongLoginOrPassword.setAlignment(Pos.CENTER);
                    hbWrongLoginOrPassword.getChildren().add(wrongLoginOrPassword);
                    loginLayout.add(hbWrongLoginOrPassword,0,4,2,1);
                }
            }
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}

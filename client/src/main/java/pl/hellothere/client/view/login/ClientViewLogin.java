package pl.hellothere.client.view.login;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.function.BiFunction;

public class ClientViewLogin extends Application {
    private Stage primaryStage;
    private BiFunction<String, String, Boolean> credentialsTest = (a,b) -> (a.equals("jarjar"));

    public void setCredentialsTest(BiFunction<String, String, Boolean> credentialsTest) { this.credentialsTest = credentialsTest; }

    public void close() {
        primaryStage.close();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setScene(makeLoginScene());
        primaryStage.setTitle("Hello There");
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    private Scene makeLoginScene() {
        Scene loginScene = new Scene(new GridPane(), 300, 220);
        loginScene.setRoot(makeLoginLayout(loginScene));

        return loginScene;
    }

    private GridPane makeLoginLayout(Scene loginScene) {
        GridPane loginLayout = makeLoginLayoutProperties();

        HBox hbHellothereLabel = makeHbHellothereLabel();
        Label loginLabel = new Label("Login:");
        Label passwordLabel = new Label("Password:");
        TextField loginField = makeLoginField();
        PasswordField passwordField = makePasswordField();
        HBox hbWrongLoginOrPassword = makeHbWrongLoginOrPassword();
        HBox hbLoginSuccessful = makeHbLoginSuccessful();
        HBox hbSignInButton = makeHbSignInButton(loginScene, loginField, passwordField, loginLayout, hbWrongLoginOrPassword, hbLoginSuccessful);

        loginLayout.add(hbHellothereLabel, 0,0, 2, 1);
        loginLayout.addRow(2, loginLabel, loginField);
        loginLayout.addRow(3, passwordLabel, passwordField);
        loginLayout.add(hbSignInButton,0,5, 2, 1);

        return loginLayout;
    }

    private EventHandler<ActionEvent> makeConfirmButtonAction(TextField loginField, PasswordField passwordField, GridPane loginLayout, HBox hbWrongLoginOrPassword, HBox hbLoginSuccessful) {
        return aEvt -> {
            System.out.println("Login: " + loginField.getText() + " " + "Password: " + passwordField.getText());
            if (!credentialsTest.apply(loginField.getText(), passwordField.getText())) {
                //Reakcja na bledne dane logowania
                loginLayout.getChildren().remove(hbWrongLoginOrPassword);
                loginLayout.getChildren().remove(hbLoginSuccessful);
                loginLayout.add(hbWrongLoginOrPassword,0,4,2,1);
            }
            else {
                //Reakcja na poprawne dane logowania
                loginLayout.getChildren().remove(hbWrongLoginOrPassword);
                loginLayout.getChildren().remove(hbLoginSuccessful);
                loginLayout.add(hbLoginSuccessful,0,4,2,1);
            }
        };
    }

    private GridPane makeLoginLayoutProperties() {
        GridPane loginLayout = new GridPane();
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setHgap(10);
        loginLayout.setVgap(10);
        loginLayout.getStylesheets().add("/LoginStyle.css");
        loginLayout.setStyle("-fx-background-color: AliceBlue;");

        return loginLayout;
    }

    private HBox makeHbHellothereLabel() {
        Label hellothereLabel = new Label("Hello There");
        hellothereLabel.setStyle("-fx-font-size: 30");
        HBox hbHellothereLabel = new HBox();
        hbHellothereLabel.setAlignment(Pos.CENTER);
        hbHellothereLabel.getChildren().add(hellothereLabel);

        return hbHellothereLabel;
    }

    private TextField makeLoginField() {
        TextField loginField = new TextField();
        loginField.setPromptText("Enter Login");

        return loginField;
    }

    private PasswordField makePasswordField() {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");

        return passwordField;
    }

    private HBox makeHbWrongLoginOrPassword() {
        Label wrongLoginOrPassword = new Label("Wrong login or password");
        wrongLoginOrPassword.setTextFill(Color.RED);
        HBox hbWrongLoginOrPassword = new HBox();
        hbWrongLoginOrPassword.setAlignment(Pos.CENTER);
        hbWrongLoginOrPassword.getChildren().add(wrongLoginOrPassword);

        return hbWrongLoginOrPassword;
    }

    private HBox makeHbLoginSuccessful() {
        Label loginSuccessful = new Label("Login Successful");
        loginSuccessful.setTextFill(Color.LIMEGREEN);
        HBox hbLoginSuccessful = new HBox();
        hbLoginSuccessful.setAlignment(Pos.CENTER);
        hbLoginSuccessful.getChildren().add(loginSuccessful);

        return hbLoginSuccessful;
    }

    private HBox makeHbSignInButton(Scene loginScene, TextField loginField, PasswordField passwordField, GridPane loginLayout, HBox hbWrongLoginOrPassword, HBox hbLoginSuccessful) {
        Button signInButton = new Button("Sign in");
        signInButton.setOnAction(makeConfirmButtonAction(loginField, passwordField, loginLayout, hbWrongLoginOrPassword, hbLoginSuccessful));
        signInButton.setOnMouseEntered(event -> loginScene.setCursor(Cursor.HAND));
        signInButton.setOnMouseExited(event -> loginScene.setCursor(Cursor.DEFAULT));
        HBox hbSignInButton = new HBox();
        hbSignInButton.setAlignment(Pos.CENTER);
        hbSignInButton.getChildren().add(signInButton);

        return hbSignInButton;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package pl.hellothere.client.view.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pl.hellothere.client.view.login.ClientViewLogin;

import java.util.function.BiFunction;

public class ClientViewController extends Application {
    static ClientViewLogin cvl0 = new ClientViewLogin();
    static BiFunction<String, String, Boolean> credentialsTest;

    @Override
    public void start(Stage primaryStage) { }

    static public ClientViewLogin getLoginView() { return cvl0; }

    static public void close() { Platform.exit(); }

    static public void showErrorMessage(String errorMessage) {
        Stage errorStage = new Stage();
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: AliceBlue;");
        Label errorLabel = new Label(errorMessage);
        errorLabel.setStyle("-fx-font-family: Lucida Console; -fx-text-fill: Red; -fx-font-size: 20; -fx-wrap-text: true");
        root.getChildren().add(errorLabel);

        errorStage.setTitle("Hello There Error");
        errorStage.setScene(new Scene(root, 300, 100));
        errorStage.setResizable(false);

        errorStage.show();
    }

    public static class NoCredentialsTestException extends Exception {}
}
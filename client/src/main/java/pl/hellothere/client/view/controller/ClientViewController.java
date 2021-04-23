package pl.hellothere.client.view.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pl.hellothere.client.view.app.ClientViewApp;
import pl.hellothere.client.view.login.ClientViewLogin;

public class ClientViewController extends Application {
    private static final ClientViewLogin cvl0 = new ClientViewLogin();
    private static final ClientViewApp cva0 = new ClientViewApp();

    @Override
    public void start(Stage primaryStage) { }

    public static ClientViewLogin getLoginView() { return cvl0; }

    public static ClientViewApp getAppView() { return cva0; }

    public static void close() { Platform.exit(); }

    public static void showErrorMessage(String errorMessage) {
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
}
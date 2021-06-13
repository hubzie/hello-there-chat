package pl.hellothere.client.view.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pl.hellothere.client.view.app.ClientViewApp;
import pl.hellothere.client.view.groupcreation.ClientViewGroupCreation;
import pl.hellothere.client.view.groupmodification.ClientViewGroupModification;
import pl.hellothere.client.view.login.ClientViewLogin;
import pl.hellothere.client.view.registration.ClientViewRegistration;
import pl.hellothere.client.view.usermodification.ClientViewUserModification;

public class ClientViewController extends Application {
    private static final ClientViewLogin cvl0 = new ClientViewLogin();
    private static final ClientViewApp cva0 = new ClientViewApp();
    private static final ClientViewRegistration cvr0 = new ClientViewRegistration();
    private static final ClientViewGroupCreation cvgc0 = new ClientViewGroupCreation();
    private static final ClientViewGroupModification cvgm0 = new ClientViewGroupModification();
    private static final ClientViewUserModification cvum0 = new ClientViewUserModification();

    @Override
    public void start(Stage primaryStage) { }

    public static ClientViewLogin getLoginView() { return cvl0; }

    public static ClientViewApp getAppView() { return cva0; }

    public static ClientViewRegistration getRegistrationView() { return cvr0; }

    public static ClientViewGroupCreation getGroupCreationView() { return cvgc0; }

    public static ClientViewGroupModification getGroupModificationView() { return cvgm0; }

    public static ClientViewUserModification getUserModificationView() { return cvum0; }

    public static void close() { Platform.exit(); }

    public static void showErrorMessage(String errorMessage) {
        Stage errorStage = new Stage();
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: WhiteSmoke;");
        Label errorLabel = new Label(errorMessage);
        errorLabel.setStyle("-fx-font-family: Lucida Console; -fx-text-fill: Red; -fx-font-size: 20; -fx-wrap-text: true");
        root.getChildren().add(errorLabel);

        errorStage.setTitle("Hello There Error");
        errorStage.setScene(new Scene(root, 300, 100));
        errorStage.setResizable(false);

        errorStage.show();
    }
}
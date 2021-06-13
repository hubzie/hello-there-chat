package pl.hellothere.client.view.usermodification;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.hellothere.client.view.controller.ClientViewController;
import pl.hellothere.client.view.registration.ClientViewRegistrationController;
import pl.hellothere.tools.QuadConsumer;

import java.util.Objects;

public class ClientViewUserModification extends Application {
    private Stage primaryStage;

    public void run() throws Exception { start(new Stage()); }

    public void close() { primaryStage.close(); }

    Stage getPrimaryStage() { return this.primaryStage; }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UserModification.fxml")));
        } catch (NullPointerException e) {
            throw new NoFxmlLoadedException();
        }

        primaryStage.setTitle("Hello There");
        primaryStage.setScene(new Scene(root, 350, 280));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(ClientViewController.getAppView().getPrimaryStage());

        primaryStage.show();
    }

    public static class NoFxmlLoadedException extends Exception {}
}

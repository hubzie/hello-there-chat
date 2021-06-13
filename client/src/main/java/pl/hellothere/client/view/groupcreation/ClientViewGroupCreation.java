package pl.hellothere.client.view.groupcreation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.hellothere.client.view.controller.ClientViewController;

import java.util.Objects;

public class ClientViewGroupCreation extends Application {
    private Stage primaryStage;
    private boolean isRunning = false;

    public void run() throws Exception {
        if(!isRunning) {
            start(new Stage());
            isRunning = true;
        }
        primaryStage.show();
    }

    public void close() {
        if(primaryStage != null) primaryStage.close();
        isRunning = false;
    }

    Stage getPrimaryStage() { return this.primaryStage; }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GroupCreation.fxml")));
        } catch (NullPointerException e) {
            throw new NoFxmlLoadedException();
        }

        primaryStage.setTitle("Hello There");
        primaryStage.setScene(new Scene(root, 300, 180));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(ClientViewController.getAppView().getPrimaryStage());

        primaryStage.hide();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class NoFxmlLoadedException extends Exception {}
}

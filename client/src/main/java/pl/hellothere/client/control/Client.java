package pl.hellothere.client.control;

import javafx.application.Application;
import javafx.stage.Stage;
import pl.hellothere.client.view.login.ClientViewLogin;

public class Client extends Application {
    @Override
    public void start(Stage primaryStage) {
        ClientViewLogin cvl = new ClientViewLogin();
        cvl.setCredentialsTest((a,b) -> (a.equals("General Kenobi")));
        cvl.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
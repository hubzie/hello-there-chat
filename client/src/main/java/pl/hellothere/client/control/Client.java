package pl.hellothere.client.control;

import javafx.application.Application;
import javafx.stage.Stage;
import pl.hellothere.client.network.ServerClient;
import pl.hellothere.client.view.controller.ClientViewController;

public class Client extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        final ServerClient connection;
        try {
            connection = new ServerClient();
        } catch (Exception e) {
            ClientViewController.showErrorMessage("No connection");
            return;
        }

        ClientViewController.getLoginView().setCredentialsTest(connection::signIn);
        ClientViewController.getLoginView().setSignInAction((a, b) -> System.out.println("Login: "+a+" Password: "+b));
        ClientViewController.getLoginView().run();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
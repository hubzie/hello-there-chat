package pl.hellothere.client.view.controller;

import javafx.application.Application;
import javafx.stage.Stage;
import pl.hellothere.client.view.login.ClientViewLogin;

import java.util.function.BiFunction;

public class ClientViewController extends Application {
    static ClientViewLogin cvl0 = new ClientViewLogin();
    static BiFunction<String, String, Boolean> credentialsTest;

    @Override
    public void start(Stage primaryStage) throws Exception {
        if(credentialsTest == null) throw new NoCredentialsTestException();
        cvl0.setCredentialsTest(credentialsTest);
        cvl0.start(primaryStage);
    }

    static public void setCredentialsTest(BiFunction<String, String, Boolean> credentialsTest) {
        ClientViewController.credentialsTest = credentialsTest;
    }

    public static class NoCredentialsTestException extends Exception {}
}


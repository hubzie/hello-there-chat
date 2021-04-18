package pl.hellothere.client.view.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pl.hellothere.client.view.controller.ClientViewController;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ClientViewLogin extends Application {
    private Stage primaryStage;
    private BiFunction<String, String, Boolean> credentialsTest;
    private BiConsumer<String, String> signInAction;
    private ClientViewLoginController cvlc;

    public void setSignInAction(BiConsumer<String, String> signInAction) {
        this.signInAction = signInAction;
    }

    public BiConsumer<String, String> getSignInAction() {
        return signInAction;
    }

    public void setCredentialsTest(BiFunction<String, String, Boolean> credentialsTest) {
        this.credentialsTest = credentialsTest;
    }

    public BiFunction<String, String, Boolean> getCredentialsTest() {
        return credentialsTest;
    }

    public void close() { primaryStage.close(); }

    public void run() throws Exception { start(new Stage()); }

    public void loginFailMessage() {
        cvlc.getResultPromptBox().getChildren().clear();
        cvlc.getResultPromptBox().getChildren().add(new Label() {
            {
                setText("Wrong login or password");
                setTextFill(Color.RED);
            }
        });
    }

    public void setCvlc(ClientViewLoginController cvlc) {
        this.cvlc = cvlc;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if(credentialsTest == null) throw new ClientViewController.NoCredentialsTestException();
        this.primaryStage = primaryStage;
        Parent root;

        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Login.fxml")));
        } catch (NullPointerException e) {
            throw new NoFxmlLoadedException();
        }
        primaryStage.setTitle("Hello There");
        primaryStage.setScene(new Scene(root, 300, 220));
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    public static class NoFxmlLoadedException extends Exception {}
}
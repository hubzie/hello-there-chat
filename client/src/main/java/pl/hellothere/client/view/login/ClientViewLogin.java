package pl.hellothere.client.view.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;
import java.util.function.BiConsumer;

public class ClientViewLogin extends Application {
    private Stage primaryStage;
    private BiConsumer<String, String> signInAction;
    private ClientViewLoginController cvlc;

    public void setSignInAction(BiConsumer<String, String> signInAction) {
        this.signInAction = signInAction;
    }

    public void close() { primaryStage.close(); }

    public void minimize() { primaryStage.setIconified(true); }

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

    BiConsumer<String, String> getSignInAction() {
        return signInAction;
    }

    void setCvlc(ClientViewLoginController cvlc) {
        this.cvlc = cvlc;
    }

    Stage getPrimaryStage() { return primaryStage; }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if(signInAction == null) throw new NoSignInActionException();
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
        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.show();
    }

    public static class NoFxmlLoadedException extends Exception {}
    public static class NoSignInActionException extends Exception {}
}
package pl.hellothere.client.view.registration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.hellothere.tools.QuadConsumer;

import java.util.Objects;

public class ClientViewRegistration extends Application {
    private Stage primaryStage;
    private ClientViewRegistrationController cvrc;
    private QuadConsumer<String, String, String, String> registerAction;

    public void setRegisterAction(QuadConsumer<String, String, String, String> registerAction) { this.registerAction = registerAction; }

    public QuadConsumer<String, String, String, String> getRegisterAction() { return this.registerAction; }

    public void run() throws Exception { start(new Stage()); }

    public void close() { primaryStage.close(); }

    public void minimize() { primaryStage.setIconified(true); }

    public void setLogin(String s) { cvrc.setLoginField(s); }

    public void setPassword(String s) { cvrc.setPasswordField(s); }

    public void clearResultPrompt() { cvrc.clearResultPrompt(); }

    void setCvrc(ClientViewRegistrationController cvrc) {
        this.cvrc = cvrc;
    }

    Stage getPrimaryStage() { return this.primaryStage; }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Registration.fxml")));
        } catch (NullPointerException e) {
            throw new NoFxmlLoadedException();
        }

        primaryStage.setTitle("Hello There");
        primaryStage.setScene(new Scene(root, 350, 330));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class NoFxmlLoadedException extends Exception {}
}

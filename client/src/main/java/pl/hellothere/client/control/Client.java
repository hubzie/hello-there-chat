package pl.hellothere.client.control;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import pl.hellothere.client.network.ServerClient;
import pl.hellothere.client.view.controller.ClientViewController;
import pl.hellothere.containers.messages.TextMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client extends Application {
    static Client client;

    ServerClient connection;
    ExecutorService ex = Executors.newCachedThreadPool();

    AtomicBoolean logging = new AtomicBoolean(false);

    void signIn(String login, String password) {
        ex.submit(() -> {
            if(!logging.compareAndSet(false, true))
                return;

            try {
                if(connection.signIn(login, password))
                    Platform.runLater(() -> {
                        ClientViewController.getLoginView().close();
                        startMainApp();
                    });
                else
                    Platform.runLater(ClientViewController.getLoginView()::loginFailMessage);
            } catch (Exception e) {
                Platform.runLater(() -> {
                    ClientViewController.showErrorMessage("No connection");
                    ClientViewController.getLoginView().close();
                });
            } finally {
                logging.set(false);
            }
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        client = this;

        try {
            connection = new ServerClient();
        } catch (Exception e) {
            ClientViewController.showErrorMessage("No connection");
            return;
        }

        ClientViewController.getLoginView().setSignInAction(this::signIn);
        ClientViewController.getLoginView().run();
    }

    void startMainApp() {
        ClientViewController.showErrorMessage("Welcome!");
        try {
            while(true){
                TextMessage msg = (TextMessage) connection.nextMessage();
                System.out.println(msg.getSenderID()+", "+msg.getDate()+" | "+msg.getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        connection.close();
        ex.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
        client.close();
    }
}
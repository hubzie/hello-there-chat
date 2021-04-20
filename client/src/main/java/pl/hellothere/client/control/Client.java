package pl.hellothere.client.control;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import pl.hellothere.client.network.ServerClient;
import pl.hellothere.client.view.controller.ClientViewController;
import pl.hellothere.containers.data.Conversation;
import pl.hellothere.containers.data.ConversationDetails;
import pl.hellothere.containers.messages.Message;
import pl.hellothere.containers.messages.TextMessage;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client extends Application {
    static Client client;

    ServerClient connection;

    AtomicBoolean logging = new AtomicBoolean(false);

    void signIn(String login, String password) {
        new Thread(() -> {
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
                e.printStackTrace();
                Platform.runLater(() -> {
                    ClientViewController.showErrorMessage("No connection");
                    ClientViewController.getLoginView().close();
                });
            } finally {
                logging.set(false);
            }
        }).start();
    }

    @Override
    public void start(Stage stage) throws Exception {
        client = this;

        try {
            connection = new ServerClient();
        } catch (ServerClient.ConnectionError e) {
            ClientViewController.showErrorMessage("No connection");
            return;
        }

        ClientViewController.getLoginView().setSignInAction(this::signIn);
        ClientViewController.getLoginView().run();
    }

    ConversationDetails conversationDetails = null;

    void startMainApp() {
        try {
            List<Conversation> list = connection.getConversationsList();
            System.out.println(list);

            if(!list.isEmpty()) {
                int id = list.get(0).getID();
                conversationDetails = connection.chooseConversation(id);
            }

            System.out.println(conversationDetails);

            if(conversationDetails != null)
                for(Message m : connection.getMessages())
                    System.out.println(m);
        } catch (ServerClient.ConnectionLost | ServerClient.ConnectionError e) {
            e.printStackTrace();
            System.out.println("No connection");
        }
    }

    public void close() {
        if(connection != null)
            connection.close();
    }

    public static void main(String[] args) {
        launch(args);
        client.close();
    }
}
package pl.hellothere.client.control;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import pl.hellothere.client.network.ServerClient;
import pl.hellothere.client.view.controller.ClientViewController;
import pl.hellothere.containers.data.Conversation;
import pl.hellothere.containers.data.ConversationDetails;
import pl.hellothere.containers.messages.Message;

import java.util.List;
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
                if(connection.signIn(login, password)) {
                        Platform.runLater(() -> {
                            ClientViewController.getLoginView().close();
                            startMainApp();
                        });
                    }
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

    void changeGroup(Integer groupId) {
        try {
            System.out.println(groupId);
            List<Conversation> list = connection.getConversationsList();
            for(Conversation c : list){
                if(c.getID() == groupId) {
                    ClientViewController.getAppView().changeGroup(c);
                    conversationDetails = connection.chooseConversation(c.getID());
                    for(Message m : connection.getMessages()) {
                        System.out.println(m);
                        ClientViewController.getAppView().addBottomMessage(m);
                    }
                    return;
                }
            }
            System.out.println("No group");
        } catch (ServerClient.ConnectionLost connectionLost) {
            connectionLost.printStackTrace();
            ClientViewController.showErrorMessage("No connection");
        }
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
        ClientViewController.getAppView().setUserID(connection.getUserID());
        ClientViewController.getAppView().setGroupAction(this::changeGroup);
        try {
            ClientViewController.getAppView().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<Conversation> list = connection.getConversationsList();
            System.out.println(list);

            for(Conversation c : list) ClientViewController.getAppView().addGroup(c);

            if(!list.isEmpty()) {
                int id = list.get(0).getID();
                changeGroup(id);
            }

            System.out.println(conversationDetails);

        } catch (ServerClient.ConnectionLost | ServerClient.ConnectionError e) {
            e.printStackTrace();
            ClientViewController.showErrorMessage("No connection");
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
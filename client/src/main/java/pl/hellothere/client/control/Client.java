package pl.hellothere.client.control;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import pl.hellothere.client.network.ServerClient;
import pl.hellothere.client.view.app.ClientViewApp;
import pl.hellothere.client.view.controller.ClientViewController;
import pl.hellothere.containers.socket.connection.requests.SendMessageRequest;
import pl.hellothere.containers.socket.data.converstions.Conversation;
import pl.hellothere.containers.socket.data.converstions.ConversationDetails;
import pl.hellothere.containers.socket.data.messages.Message;
import pl.hellothere.containers.socket.data.notifications.MessageNotification;
import pl.hellothere.tools.CommunicationException;
import pl.hellothere.tools.ConnectionError;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client extends Application {
    static Client client = null;
    ServerClient connection;

    AtomicBoolean logging = new AtomicBoolean(false);

    void signIn(String login, String password) {
        new Thread(() -> {
            if (!logging.compareAndSet(false, true))
                return;

            try {
                if (connection.signIn(login, password)) {
                    Platform.runLater(() -> {
                        ClientViewController.getLoginView().close();
                        startMainApp();
                    });
                } else
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
    public void start(Stage stage) {
        client = this;

        try {
            connection = new ServerClient();
        } catch (ConnectionError e) {
            ClientViewController.showErrorMessage("No connection");
            return;
        }

        ClientViewController.getLoginView().setSignInAction(this::signIn);
        try {
            ClientViewController.getLoginView().run();
        } catch (Exception e) {
            e.printStackTrace();
            ClientViewController.showErrorMessage("View error");
        }
    }

    ConversationDetails conversationDetails = null;

    void changeGroup(int groupId) {
        try {
            conversationDetails = connection.changeConversation(groupId);
            for (Message m : connection.getMessageList()) {
                try {
                    ClientViewController.getAppView().addTopMessage(m);
                } catch (ClientViewApp.UnknownMessageTypeException e) {
                    e.printStackTrace();
                    ClientViewController.showErrorMessage("Unknown Message Type");
                }
            }
        } catch (CommunicationException e) {
            e.printStackTrace();
            ClientViewController.getAppView().close();
            ClientViewController.showErrorMessage("No connection");
        }
    }

    void sendMessage(Message msg) {
        try {
            connection.sendMessage(msg);
        } catch (CommunicationException e) {
            e.printStackTrace();
            ClientViewController.getAppView().close();
            ClientViewController.showErrorMessage("No connection");
        }
    }

    void logout(Void v) {
        try {
            //todo logout from server
            ClientViewController.getAppView().close();
            ClientViewController.getLoginView().run();
        } catch (Exception e) {
            e.printStackTrace();
            ClientViewController.showErrorMessage("View error");
        }
    }

    void startMainApp() {
        ClientViewController.getAppView().setUserID(connection.getUser().getID());
        ClientViewController.getAppView().setGroupAction(this::changeGroup);
        ClientViewController.getAppView().setSendAction(this::sendMessage);
        ClientViewController.getAppView().setLogoutAction(this::logout);

        try {
            ClientViewController.getAppView().run();
            connection.listen(x -> Platform.runLater(() -> {
                try {
                    if (x instanceof MessageNotification)
                        ClientViewController.getAppView().addBottomMessage(((MessageNotification) x).getContent());
                    else
                        System.out.println(x);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            List<Conversation> list = connection.getConversationList();
            for (Conversation c : list)
                ClientViewController.getAppView().addGroup(c);

            if (!list.isEmpty()) {
                int id = list.get(0).getID();
                changeGroup(id);
            }
        } catch (ConnectionError e) {
            e.printStackTrace();
            ClientViewController.getAppView().close();
            ClientViewController.showErrorMessage("No connection");
        } catch (Exception e) {
            e.printStackTrace();
            ClientViewController.showErrorMessage("View error");
        }
    }

    public void close() {
        if (connection != null)
            connection.close();
    }

    public static void main(String[] args) {
        launch(args);
        client.close();
    }
}
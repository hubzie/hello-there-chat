package pl.hellothere.client.control;

import javafx.application.Application;
import javafx.stage.Stage;
import pl.hellothere.client.network.ServerClient;
import pl.hellothere.containers.socket.data.UserData;
import pl.hellothere.tools.ConnectionError;

public class Client extends Application {
/*
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

    @Override
    public void start(Stage stage) {
        client = this;

        try {
            connection = new ServerClient();
        } catch (ServerClient.ConnectionError e) {
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
            conversationDetails = connection.chooseConversation(groupId);
            for(Message m : connection.getMessages()) {
                try {
                    ClientViewController.getAppView().addBottomMessage(m);
                } catch (ClientViewApp.UnknownMessageTypeException e) {
                    e.printStackTrace();
                    ClientViewController.showErrorMessage("Unknown Message Type");
                }
            }
        } catch (ServerClient.ConnectionLost e) {
            e.printStackTrace();
            ClientViewController.getAppView().close();
            ClientViewController.showErrorMessage("No connection");
        }
    }

    void startMainApp() {
        ClientViewController.getAppView().setUserID(connection.getUser().getID());
        ClientViewController.getAppView().setGroupAction(this::changeGroup);

        try {
            ClientViewController.getAppView().run();

            List<Conversation> list = connection.getConversationsList();
            for(Conversation c : list)
                ClientViewController.getAppView().addGroup(c);

            if(!list.isEmpty()) {
                int id = list.get(0).getID();
                changeGroup(id);
            }
        } catch (ServerClient.ConnectionLost | ServerClient.ConnectionError e) {
            e.printStackTrace();
            ClientViewController.getAppView().close();
            ClientViewController.showErrorMessage("No connection");
        } catch (Exception e) {
            e.printStackTrace();
            ClientViewController.showErrorMessage("View error");
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
*/
    static Client client = null;
    ServerClient connection;

    @Override
    public void start(Stage stage) {
        client = this;

        try {
            connection = new ServerClient();
        } catch (ConnectionError e) {
            System.out.println("No connection");
            return;
        }

        String login = "obiwan";
        String password = "password";

        try {
            UserData user;
            if((user = connection.signIn(login, password)) != null)
                System.out.println("("+user.getID()+"): "+user.getName());
            else
                System.out.println("Wrong data");
        } catch (ConnectionError e) {
            e.printStackTrace();
        }
    }

    public void close() {
        connection.close();
    }

    public static void main(String[] args) {
        launch(args);
        client.close();
    }
}
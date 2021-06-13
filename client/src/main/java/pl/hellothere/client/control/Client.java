package pl.hellothere.client.control;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import pl.hellothere.client.network.NotificationHandler;
import pl.hellothere.client.network.ServerClient;
import pl.hellothere.client.view.app.ClientViewApp;
import pl.hellothere.client.view.controller.ClientViewController;
import pl.hellothere.containers.socket.authorization.ModifyUserResult;
import pl.hellothere.containers.socket.data.UserData;
import pl.hellothere.containers.socket.data.converstions.Conversation;
import pl.hellothere.containers.socket.data.converstions.ConversationDetails;
import pl.hellothere.containers.socket.data.messages.Message;
import pl.hellothere.containers.socket.data.notifications.ErrorNotification;
import pl.hellothere.containers.socket.data.notifications.MessageNotification;
import pl.hellothere.containers.socket.data.notifications.Notification;
import pl.hellothere.containers.socket.data.notifications.RefreshNotification;
import pl.hellothere.tools.CommunicationException;
import pl.hellothere.tools.ConnectionError;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
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

    void register(String name, String email, String login, String password) {
        try {
            switch (connection.register(name, login, email, password)) {
                case OK:
                    Platform.runLater(() -> {
                        ClientViewController.showErrorMessage("Konto zostało założone i oczekuje na aktywację");
                        ClientViewController.getRegistrationView().close();
                        try {
                            ClientViewController.getLoginView().run();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                case LOGIN_ALREADY_USED:
                    ClientViewController.getRegistrationView().clearResultPrompt();
                    ClientViewController.showErrorMessage("Podany login jest już użyty");
                    break;
                case EMAIL_ALREADY_USED:
                    ClientViewController.getRegistrationView().clearResultPrompt();
                    ClientViewController.showErrorMessage("Podany email jest już użyty");
                    break;
            }
        } catch (CommunicationException e) {
            e.printStackTrace();
            ClientViewController.getRegistrationView().close();
            ClientViewController.showErrorMessage("No connection");
        }
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
        ClientViewController.getRegistrationView().setRegisterAction(this::register);
        try {
            ClientViewController.getLoginView().run();
        } catch (Exception e) {
            e.printStackTrace();
            ClientViewController.showErrorMessage("View error");
        }
    }

    ConversationDetails conversationDetails = null;

    public void loadMoreMessages(Date time) {
        try {
            for(Message msg : connection.loadMoreMessages(time))
                try {
                    ClientViewController.getAppView().addTopMessage(msg);
                } catch (ClientViewApp.UnknownMessageTypeException e) {
                    e.printStackTrace();
                    ClientViewController.showErrorMessage("Unknown Message Type");
                }
        } catch (CommunicationException e) {
            e.printStackTrace();
            ClientViewController.getAppView().close();
            ClientViewController.showErrorMessage("No connection");
        }
    }

    void reloadGroup() throws CommunicationException {
        conversationDetails = connection.changeConversation(conversationDetails.getID());
        if(conversationDetails.isValid())
            ClientViewController.getAppView().setConversationDetails(conversationDetails);
    }

    void changeGroup(int groupId) {
        try {
            conversationDetails = connection.changeConversation(groupId);
            ClientViewController.getAppView().setConversationDetails(conversationDetails);
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
            connection.logOut();
            ClientViewController.getAppView().close();
            ClientViewController.getLoginView().run();
        } catch (CommunicationException e) {
            e.printStackTrace();
            ClientViewController.getAppView().close();
            ClientViewController.showErrorMessage("No connection");
        } catch (Exception e) {
            e.printStackTrace();
            ClientViewController.showErrorMessage("View error");
        }
    }

    void reloadConversationList() throws CommunicationException {
        List<Conversation> list = connection.getConversationList();

        if (conversationDetails == null || !conversationDetails.isValid())
            changeGroup(list.get(0).getID());

        while (!list.contains(conversationDetails))
            list = connection.loadMoreConversationsAndReload();

        ClientViewController.getAppView().clearGroups();
        for (Conversation c : list) {
            ClientViewController.getAppView().setConversationDetails(connection.changeConversation(c.getID()));
            ClientViewController.getAppView().addGroup(c);
        }

        ClientViewController.getAppView().changeGroup(conversationDetails);
    }

    List<UserData> listAddableUsers(String pref) {
        List<UserData> userList = new LinkedList<>();

        try {
            userList = connection.getAddableUserList(pref);
        } catch (CommunicationException e) {
            e.printStackTrace();
            ClientViewController.showErrorMessage("No connection");
        }

        return userList;
    }

    void addConversation(String name) {
        try {
            connection.createConversation(name);
            reloadConversationList();
        } catch (CommunicationException e) {
            e.printStackTrace();
            ClientViewController.showErrorMessage("No connection");
        }
    }

    void addMember(int id) {
        try {
            connection.addMember(id);
        } catch (CommunicationException e) {
            e.printStackTrace();
            ClientViewController.showErrorMessage("No connection");
        }
    }

    void renameConversation(int id, String name) {
        try {
            connection.renameConversation(id, name);
        } catch (CommunicationException e) {
            e.printStackTrace();
            ClientViewController.showErrorMessage("No connection");
        }
    }

    ModifyUserResult.Code modifyUser(int id, String name, String login, String password) {
        try {
            return connection.modifyUser(id,name,login,password);
        } catch (CommunicationException e) {
            e.printStackTrace();
            ClientViewController.showErrorMessage("No connection");
            return ModifyUserResult.Code.SERVER_ERROR;
        }
    }

    void startMainApp() {
        ClientViewController.getAppView().setUserID(connection.getUser().getID());
        ClientViewController.getAppView().setGroupAction(this::changeGroup);
        ClientViewController.getAppView().setSendAction(this::sendMessage);
        ClientViewController.getAppView().setLogoutAction(this::logout);
        ClientViewController.getAppView().setLoadMessagesAction(this::loadMoreMessages);
        ClientViewController.getAppView().setListUsersFunction(this::listAddableUsers);
        ClientViewController.getAppView().setAddConversationAction(this::addConversation);
        ClientViewController.getAppView().setAddMemberAction(this::addMember);
        ClientViewController.getAppView().setRenameConversationAction(this::renameConversation);
        ClientViewController.getAppView().setModifyUserAction(this::modifyUser);

        try {
            ClientViewController.getAppView().run();
            connection.listen(new NotificationHandler(){
                @Override
                public void handle(Notification action) {
                    Platform.runLater(() -> {
                        try {
                            if (action instanceof MessageNotification)
                                ClientViewController.getAppView().addBottomMessage(((MessageNotification) action).getContent());
                            else
                                System.out.println(action);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                public void handle(ErrorNotification action) {
                    Platform.runLater(() -> ClientViewController.showErrorMessage(action.getMessage()));
                }

                @Override
                public void handle(RefreshNotification action) {
                    Platform.runLater(() -> {
                        try {
                            switch (action.getContext()) {
                                case CONVERSATION_DATA: reloadGroup();
                                case CONVERSATION_LIST: reloadConversationList(); break;
                            }
                        } catch (CommunicationException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });

            reloadConversationList();
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
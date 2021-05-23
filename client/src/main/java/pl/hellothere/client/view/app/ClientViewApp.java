package pl.hellothere.client.view.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.hellothere.containers.socket.data.UserData;
import pl.hellothere.containers.socket.data.converstions.Conversation;
import pl.hellothere.containers.socket.data.converstions.ConversationDetails;
import pl.hellothere.containers.socket.data.messages.Message;
import pl.hellothere.containers.socket.data.messages.TextMessage;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClientViewApp extends Application {
    private Stage primaryStage;
    private ClientViewAppController cvac;
    private int curUserID = -1;
    private Consumer<Integer> groupAction;
    private Consumer<Message> sendAction;
    private Consumer<Void> logoutAction;
    private Consumer<Date> loadMessagesAction;
    private Conversation curGroup = null;
    private final HashMap<Conversation, GroupButton> convButtonMap = new HashMap<>();
    private boolean scrollMessagesToBottom = false;
    private Date lastLoadedMessageDate = null;
    ConversationDetails conversationDetails = null;
    private final HashMap<Integer, UserData> userIdDataMap = new HashMap<>();

    public void run() throws Exception { start(new Stage()); }

    public void close() { primaryStage.close(); }

    public void minimize() { primaryStage.setIconified(true); }

    public void setGroupAction(Consumer<Integer> groupAction) { this.groupAction = groupAction; }

    public void setSendAction(Consumer<Message> sendAction) { this.sendAction = sendAction; }

    public Consumer<Message> getSendAction() { return sendAction; }

    public void setLogoutAction(Consumer<Void> logoutAction) { this.logoutAction = logoutAction; }

    public Consumer<Void> getLogoutAction() { return logoutAction; }

    public void setLoadMessagesAction(Consumer<Date> loadMessagesAction) { this.loadMessagesAction = loadMessagesAction; }

    public Consumer<Date> getLoadMessagesAction() { return loadMessagesAction; }

    public void setConversationDetails(ConversationDetails conversationDetails) {
        this.conversationDetails = conversationDetails;

        userIdDataMap.clear();
        for(UserData i : conversationDetails.getUsers()) userIdDataMap.put(i.getID(), i);
    }

    public void setScrollMessagesToBottom() { scrollMessagesToBottom = true; }

    public void changeGroup(Conversation curGroup) {
        cvac.messagesBox.getChildren().clear();
        if(this.curGroup != null) convButtonMap.get(this.curGroup).getStyleClass().remove("selected-group-button");
        convButtonMap.get(curGroup).getStyleClass().add("selected-group-button");
        this.curGroup = curGroup;
        groupAction.accept(curGroup.getID());
        cvac.messagesPane.setVvalue(1D);
        setScrollMessagesToBottom();

        cvac.groupsBox.getChildren().removeIf(e -> (e instanceof GroupMember));
        int ind = cvac.groupsBox.getChildren().indexOf(convButtonMap.get(curGroup));
        for(UserData i : conversationDetails.getUsers()) cvac.groupsBox.getChildren().add(++ind, new GroupMember(i));
    }

    public void addTopMessage(Message m) throws UnknownMessageTypeException {
        if(m instanceof TextMessage) {
            cvac.messagesBox.getChildren().add( 0, new TextMessageBox(m) );
            lastLoadedMessageDate = m.getSendTime();
        }
        else {
            throw new UnknownMessageTypeException();
        }
    }

    public void addBottomMessage(Message m) throws UnknownMessageTypeException {
        if(m instanceof TextMessage) {
            cvac.messagesBox.getChildren().add( new TextMessageBox(m) );
        }
        else {
            throw new UnknownMessageTypeException();
        }
    }

    public void addGroup(Conversation c) {
        convButtonMap.put(c,new GroupButton(c));
        cvac.groupsBox.getChildren().add(convButtonMap.get(c));
    }

    public void setUserID(int curUserID) { this.curUserID = curUserID; }

    void setCvlc(ClientViewAppController cvac) {
        this.cvac = cvac;
    }

    Stage getPrimaryStage() { return primaryStage; }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Parent root;

        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/App.fxml")));
        } catch (NullPointerException e) {
            throw new NoFxmlLoadedException();
        }
        root.getStyleClass().add("box");
        primaryStage.setTitle("Hello There");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);

        cvac.messagesBox.heightProperty().addListener(observable -> {
            if(scrollMessagesToBottom) {
                cvac.messagesPane.setVvalue(1D);
                scrollMessagesToBottom = false;
            }
        });

        cvac.messagesPane.vvalueProperty().addListener(observable -> {
            if(cvac.messagesPane.vvalueProperty().getValue().equals(0D)) {
                Double oldHeight = cvac.messagesBox.heightProperty().getValue();
                loadMessagesAction.accept(lastLoadedMessageDate);
                Platform.runLater(() -> {
                    Double newHeight = cvac.messagesBox.heightProperty().getValue();
                    cvac.messagesPane.setVvalue((newHeight-oldHeight)/newHeight);
                });
            }
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public class TextMessageBox extends HBox {
        TextMessageBox(Message m) {
            getChildren().add(new Label() {
                {
                    setText( ((TextMessage) m).getContent() );
                    setWrapText(true);
                    setMinHeight(45);
                    setMaxWidth(500);
                    if(m.getSenderID() == curUserID) getStyleClass().add("my-message");
                    else getStyleClass().add("your-message");
                    HBox.setMargin(this, new Insets(0, 3, 0, 3));
                }
            });
            getChildren().add((m.getSenderID() == curUserID) ? 0 : 1, new BorderPane(){
                {
                    setBottom(new Text(){
                        {
                            GregorianCalendar time = new GregorianCalendar();
                            time.setTime(m.getSendTime());
                            setText(time.get(Calendar.YEAR)+"-"+
                                    ((time.get(Calendar.MONTH)+1 < 10) ? 0 : "")+(time.get(Calendar.MONTH)+1)+"-"+
                                    ((time.get(Calendar.DAY_OF_MONTH) < 10) ? 0 : "")+time.get(Calendar.DAY_OF_MONTH)+" "+
                                    ((time.get(Calendar.HOUR_OF_DAY) < 10) ? 0 : "")+time.get(Calendar.HOUR_OF_DAY)+":"+
                                    ((time.get(Calendar.MINUTE) < 10) ? 0 : "")+time.get(Calendar.MINUTE));
                            getStyleClass().add("message-time");
                        }
                    });
                }
            });
            if(m.getSenderID() != curUserID) {
                getChildren().add(0, new BorderPane() {
                    {
                        setBottom( new Label() {
                            {
                                StringBuilder sb = new StringBuilder();
                                Pattern patt = Pattern.compile("\\b[a-zA-Z0-9]");
                                Matcher match = patt.matcher(userIdDataMap.get(m.getSenderID()).getName());
                                while (match.find()) sb.append(match.group());
                                setText(sb.toString());

                                getStyleClass().add("message-sender");
                            }
                        });
                    }
                });
            }

            getStyleClass().add("message-box");
            if(m.getSenderID() == curUserID)setAlignment(Pos.CENTER_RIGHT);
            else setAlignment(Pos.CENTER_LEFT);
        }
    }

    public class GroupButton extends Button {
        Conversation conv;

        GroupButton(Conversation conv) {
            this.conv = conv;
            getStyleClass().add("group-button");
            setMaxWidth(Double.MAX_VALUE);
            setText((conv.getName() == null) ? "Group" : conv.getName());
            setOnAction(e -> changeGroup(conv));
        }
    }

    public class GroupMember extends Label {
        UserData userData;

        GroupMember(UserData userData) {
            this.userData = userData;
            setText(userData.getName());
            getStyleClass().add("group-member");
            setMaxWidth(Double.MAX_VALUE);
            setAlignment(Pos.CENTER);
        }
    }

    public static class NoFxmlLoadedException extends Exception {}
    public static class UnknownMessageTypeException extends Exception {}
}

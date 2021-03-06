package pl.hellothere.client.view.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.hellothere.client.view.controller.ClientViewController;
import pl.hellothere.containers.socket.authorization.ModifyUserResult;
import pl.hellothere.containers.socket.data.UserData;
import pl.hellothere.containers.socket.data.converstions.Conversation;
import pl.hellothere.containers.socket.data.converstions.ConversationDetails;
import pl.hellothere.containers.socket.data.messages.Message;
import pl.hellothere.containers.socket.data.messages.MessageType;
import pl.hellothere.containers.socket.data.messages.StickerMessage;
import pl.hellothere.containers.socket.data.messages.TextMessage;
import pl.hellothere.tools.QuadFunction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
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
    private ConversationDetails conversationDetails = null;
    private final HashMap<Integer, UserData> userIdDataMap = new HashMap<>();
    private final Stage stickerStage = new Stage();
    private Function<String, List<UserData>> listUsersFunction;
    private Consumer<String> addConversationAction;
    private Consumer<Integer> addMemberAction;
    private Consumer<Integer> removeMemberAction;
    private BiConsumer<Integer, String> renameConversationAction;
    private QuadFunction<Integer,String,String,String,ModifyUserResult.Code> modifyUserAction;

    public void run() throws Exception { start(new Stage()); }

    public void close() {
        primaryStage.close();
        stickerStage.close();
        ClientViewController.getGroupCreationView().close();
        ClientViewController.getGroupModificationView().close();
    }

    public void minimize() {
        primaryStage.setIconified(true);
        stickerStage.hide();
        ClientViewController.getGroupCreationView().close();
        ClientViewController.getGroupModificationView().close();
    }

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
        if(curGroup != this.curGroup) {
            cvac.messagesBox.getChildren().clear();
            if(this.curGroup != null && convButtonMap.get(this.curGroup) != null) convButtonMap.get(this.curGroup).getStyleClass().remove("selected-group-button");
            convButtonMap.get(curGroup).getStyleClass().add("selected-group-button");
            this.curGroup = curGroup;
            groupAction.accept(curGroup.getID());
            cvac.messagesPane.setVvalue(1D);
            setScrollMessagesToBottom();

            cvac.groupsBox.getChildren().removeIf(e -> (e instanceof GroupMember));
            if( (curGroup.getName() != null && !curGroup.getName().equals("") && conversationDetails.getUsers().size() > 1 ) || conversationDetails.getUsers().size() > 2 ) {
                int ind = cvac.groupsBox.getChildren().indexOf(convButtonMap.get(curGroup));
                for(UserData i : conversationDetails.getUsers()) if(i.getID() != curUserID) cvac.groupsBox.getChildren().add(++ind, new GroupMember(i));
            }
        }
    }

    public void addTopMessage(Message m) throws UnknownMessageTypeException {
        if(m instanceof TextMessage || m instanceof StickerMessage) {
            cvac.messagesBox.getChildren().add( 0, new TextMessageBox(m) );
            lastLoadedMessageDate = m.getSendTime();
        }
        else {
            throw new UnknownMessageTypeException();
        }
    }

    public void addBottomMessage(Message m) throws UnknownMessageTypeException {
        if(m instanceof TextMessage || m instanceof StickerMessage) {
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

    public void clearGroups() {
        convButtonMap.clear();
        cvac.groupsBox.getChildren().clear();
    }

    public void setUserID(int curUserID) { this.curUserID = curUserID; }

    public void setListUsersFunction(Function<String, List<UserData>> listUsersFunction) { this.listUsersFunction = listUsersFunction; }

    public List<UserData> getUserList(String pref) { return listUsersFunction.apply(pref); }

    public void setAddConversationAction(Consumer<String> addConversationAction) { this.addConversationAction = addConversationAction; }

    public Consumer<String> getAddConversationAction() { return addConversationAction; }

    public void setAddMemberAction(Consumer<Integer> addMemberAction) { this.addMemberAction = addMemberAction; }

    public Consumer<Integer> getAddMemberAction() { return addMemberAction; }

    public void setRemoveMemberAction(Consumer<Integer> removeMemberAction) { this.removeMemberAction = removeMemberAction; }

    public Consumer<Integer> getRemoveMemberAction() { return removeMemberAction; }

    public Conversation getCurrentGroup() { return curGroup; }

    public void setRenameConversationAction(BiConsumer<Integer, String> renameConversationAction) { this.renameConversationAction = renameConversationAction; }

    public BiConsumer<Integer, String> getRenameConversationAction() { return renameConversationAction; }

    public void setModifyUserAction(QuadFunction<Integer,String,String,String,ModifyUserResult.Code> modifyUserAction) { this.modifyUserAction = modifyUserAction; }

    public QuadFunction<Integer,String,String,String,ModifyUserResult.Code> getModifyUserAction() { return modifyUserAction; }

    public int getCurUserID() { return curUserID; }

    public Stage getPrimaryStage() { return primaryStage; }

    public ConversationDetails getConversationDetails() { return conversationDetails; }

    public void clearMessages() { cvac.ClearMessagesBox(); }

    void setCvac(ClientViewAppController cvac) { this.cvac = cvac; }

    Stage getStickerStage() { return stickerStage; }

    void showStickerSelect() {
        stickerStage.setX(primaryStage.getX());
        stickerStage.setY(primaryStage.getY() + primaryStage.getHeight());
        stickerStage.show();
    }

    void hideStickerSelect() {
        stickerStage.hide();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Parent root;

        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/App.fxml")));
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


        stickerStage.initStyle(StageStyle.UNDECORATED);
        ScrollPane stickerScroll = new ScrollPane();
        FlowPane stickerPane = new FlowPane();
        stickerPane.setHgap(2);
        stickerPane.setPrefWrapLength(245);
        stickerScroll.setContent(stickerPane);
        stickerStage.setScene(new Scene(stickerScroll, 245, 250));
        for (File curFile : Objects.requireNonNull(new File("src/main/resources/stickers").listFiles())) {
            String mimetype = Files.probeContentType(curFile.toPath());
            if(curFile.isFile() && mimetype != null && mimetype.split("/")[0].equals("image"))
                stickerPane.getChildren().add(new StickerButton(curFile.getName()));
        }
        stickerStage.getScene().getStylesheets().add("css/AppStyleGrey.css");
        stickerStage.getScene().getRoot().setStyle("-fx-border-width: 2; -fx-border-color: WhiteSmoke;");

        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/resources/style.properties"));
            if(prop.getProperty("style.mode").equals("dark")) cvac.handleChangeStyleButtonAction();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class TextMessageBox extends HBox {
        TextMessageBox(Message m) {
            getChildren().add(new Label() {
                {
                    if(m instanceof TextMessage) {
                        setText( m.getContent() );
                        setWrapText(true);
                    }

                    if(m instanceof StickerMessage) {
                        Image img = new Image("stickers/"+m.getContent());
                        ImageView imgView = new ImageView(img);
                        imgView.setPreserveRatio(true);
                        imgView.setFitHeight(200);
                        imgView.setFitWidth(200);
                        setGraphic(imgView);
                    }

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
                                if(userIdDataMap.get(m.getSenderID()) == null) {
                                    setText("?");
                                }
                                else {
                                    StringBuilder sb = new StringBuilder();
                                    Pattern patt = Pattern.compile("\\b[a-zA-Z0-9]");
                                    Matcher match = patt.matcher(userIdDataMap.get(m.getSenderID()).getName());
                                    while (match.find()) sb.append(match.group());
                                    setText(sb.toString());
                                }

                                setMinWidth(25);
                                setAlignment(Pos.CENTER);
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
            setOnAction(e -> changeGroup(conv));
            if((conv.getName() == null || conv.getName().equals("")) && conversationDetails.getUsers().size() > 2) {
                StringBuilder autoGroupName = new StringBuilder();
                Pattern patt = Pattern.compile("\\b[a-zA-Z0-9]");

                for(UserData i : conversationDetails.getUsers()) {
                    if(i.getID() != curUserID) {
                        Matcher match = patt.matcher(i.getName());
                        while (match.find()) autoGroupName.append(match.group());
                        autoGroupName.append(", ");
                    }
                }

                autoGroupName.setLength(autoGroupName.length() - 2);

                setText(autoGroupName.toString());
            }
            else if((conv.getName() == null || conv.getName().equals("")) && conversationDetails.getUsers().size() == 2) {
                for(UserData i : conversationDetails.getUsers()) {
                    if(i.getID() != curUserID) {
                        setText(i.getName());
                        break;
                    }
                }
            }
            else if((conv.getName() == null || conv.getName().equals(""))) setText("Only You");
            else setText(conv.getName());
        }
    }

    public static class GroupMember extends Label {
        UserData userData;

        GroupMember(UserData userData) {
            this.userData = userData;
            setText(userData.getName());
            getStyleClass().add("group-member");
            setMaxWidth(Double.MAX_VALUE);
            setAlignment(Pos.CENTER);
        }
    }

    public class StickerButton extends Button {
        String stickerName;

        StickerButton(String stickerName) {
            this.stickerName = stickerName;
            Image img = new Image("stickers/"+stickerName);
            ImageView imgView = new ImageView(img);
            imgView.setPreserveRatio(true);
            imgView.setFitHeight(50);
            imgView.setFitWidth(50);
            setGraphic(imgView);
            getStyleClass().add("sticker-button");

            setOnAction(e -> {
                sendAction.accept(Message.createMessage(stickerName, MessageType.Sticker));
                setScrollMessagesToBottom();
            });
        }
    }

    public static class NoFxmlLoadedException extends Exception {}
    public static class UnknownMessageTypeException extends Exception {}
}
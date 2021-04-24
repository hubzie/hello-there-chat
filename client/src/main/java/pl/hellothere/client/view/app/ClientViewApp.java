package pl.hellothere.client.view.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pl.hellothere.containers.data.Conversation;
import pl.hellothere.containers.messages.Message;
import pl.hellothere.containers.messages.TextMessage;

import java.util.Objects;
import java.util.function.Consumer;


public class ClientViewApp extends Application {
    private Stage primaryStage;
    private ClientViewAppController cvac;
    private int curUserID = -1;
    private Consumer<Integer> groupAction;
    private Conversation curGroup = null;

    public void run() throws Exception { start(new Stage()); }

    public void close() { primaryStage.close(); }

    public void setGroupAction(Consumer<Integer> groupAction) { this.groupAction = groupAction; }

    public void changeGroup(Conversation curGroup) {
        cvac.messagesBox.getChildren().clear();
        this.curGroup = curGroup;
    }

    public void addTopMessage(Message m) throws UnknownMessageTypeException {
        if(m instanceof TextMessage) {
            cvac.messagesBox.getChildren().add( 0, new TextMessageBox(m) );
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
        cvac.appTop.getChildren().add(new GroupButton(c));
    }

    public void setUserID(int curUserID) { this.curUserID = curUserID; }

    void setCvlc(ClientViewAppController cvac) {
        this.cvac = cvac;
    }

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
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);

        cvac.messagesBox.heightProperty().addListener(observable -> cvac.messagesPane.setVvalue(1D));

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
                    if(m.getSenderID() == curUserID)getStyleClass().add("my-message");
                    else getStyleClass().add("your-message");
                }
            });
            getStyleClass().add("box");
            if(m.getSenderID() == curUserID)setAlignment(Pos.CENTER_RIGHT);
            else setAlignment(Pos.CENTER_LEFT);
        }
    }

    public class GroupButton extends Button {
        Conversation conv;

        GroupButton(Conversation conv) {
            this.conv = conv;
            setText(conv.getName());
            setOnAction(e -> {
                changeGroup(conv);
                groupAction.accept(conv.getID());
            });
        }
    }

    public static class NoFxmlLoadedException extends Exception {}
    public static class UnknownMessageTypeException extends Exception {}
}

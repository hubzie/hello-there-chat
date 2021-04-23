package pl.hellothere.client.view.app;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.hellothere.client.view.controller.ClientViewController;

public class ClientViewAppController {
    public VBox messagesBox;
    public TextField newMessageField;
    public HBox appTop;
    public ScrollPane messagesPane;

    public ClientViewAppController() { ClientViewController.getAppView().setCvlc(this); }

    public void handleSendButtonAction() {
        messagesBox.getChildren().add(new HBox(){
            {
                getChildren().add(new Label() {
                    {
                        setText( newMessageField.getText() );
                        setWrapText(true);
                        setMinHeight(45);
                        setMaxWidth(500);
                        getStyleClass().add("my-message");
                    }
                });
                getStyleClass().add("box");
                setAlignment(Pos.CENTER_RIGHT);
            }
        });
        newMessageField.clear();
        Platform.runLater(() -> messagesPane.setVvalue(messagesPane.getVmax()));
    }
}

package pl.hellothere.client.view.groupcreation;

import javafx.scene.control.TextField;
import pl.hellothere.client.view.controller.ClientViewController;

public class ClientViewGroupCreationController {
    public TextField groupNameField;

    public void handleMinimizeButtonAction() { ClientViewController.getGroupCreationView().minimize(); }

    public void handleCloseButtonAction() { ClientViewController.getGroupCreationView().close(); }

    public void handleCreateGroupButtonAction() {
        ClientViewController.getAppView().getAddConversationAction().accept(groupNameField.getText());
        ClientViewController.getGroupCreationView().close();
    }
}
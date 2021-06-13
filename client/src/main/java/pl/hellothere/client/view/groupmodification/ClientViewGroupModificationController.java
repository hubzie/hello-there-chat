package pl.hellothere.client.view.groupmodification;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import pl.hellothere.client.view.controller.ClientViewController;

public class ClientViewGroupModificationController {
    public TextField groupNameField;

    public ListView<ClientViewGroupModification.PrettyUserData> possibleMembersList = new ListView<>();

    public ClientViewGroupModificationController() {
        ClientViewController.getGroupModificationView().setCvgcc(this);
    }

    public ListView<ClientViewGroupModification.PrettyUserData> getMembersList() { return possibleMembersList; }

    public void handleMinimizeButtonAction() { ClientViewController.getGroupModificationView().minimize(); }

    public void handleCloseButtonAction() { ClientViewController.getGroupModificationView().close(); }

    public void handleCreateGroupButtonAction() {
        for(ClientViewGroupModification.PrettyUserData i : possibleMembersList.getSelectionModel().getSelectedItems()) {
            ClientViewController.getAppView().getAddMemberAction().accept(i.getData().getID());
        }
        ClientViewController.getGroupModificationView().close();
    }
}
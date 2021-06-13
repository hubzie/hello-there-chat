package pl.hellothere.client.view.groupmodification;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import pl.hellothere.client.view.controller.ClientViewController;

public class ClientViewGroupModificationController {
    public TextField groupNameField;
    public ListView<ClientViewGroupModification.PrettyUserData> possibleMembersList = new ListView<>();
    public ListView<ClientViewGroupModification.PrettyUserData> currentMembersList = new ListView<>();

    public ClientViewGroupModificationController() { ClientViewController.getGroupModificationView().setCvgcc(this); }

    public ListView<ClientViewGroupModification.PrettyUserData> getPossibleMembersList() { return possibleMembersList; }

    public ListView<ClientViewGroupModification.PrettyUserData> getCurrentMembersList() { return currentMembersList; }

    public void setGroupNameField(String groupName) { groupNameField.setText(groupName); }

    public void handleCloseButtonAction() { ClientViewController.getGroupModificationView().close(); }

    public void handleCreateGroupButtonAction() {
        boolean deleteMyself = false;

        ClientViewController.getAppView().getRenameConversationAction().accept(ClientViewController.getAppView().getCurrentGroup().getID(), groupNameField.getText());

        for(ClientViewGroupModification.PrettyUserData i : possibleMembersList.getSelectionModel().getSelectedItems()) {
            ClientViewController.getAppView().getAddMemberAction().accept(i.getData().getID());
        }

        for(ClientViewGroupModification.PrettyUserData i : currentMembersList.getSelectionModel().getSelectedItems()) {
            if(i.getData().getID() == ClientViewController.getAppView().getCurUserID()) {
                deleteMyself = true;
                continue;
            }
            ClientViewController.getAppView().getRemoveMemberAction().accept(i.getData().getID());
        }

        if(deleteMyself) ClientViewController.getAppView().getRemoveMemberAction().accept(ClientViewController.getAppView().getCurUserID());

        ClientViewController.getGroupModificationView().close();
    }
}
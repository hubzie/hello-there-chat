package pl.hellothere.client.view.groupmodification;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.hellothere.client.view.controller.ClientViewController;
import pl.hellothere.client.view.groupcreation.ClientViewGroupCreationController;
import pl.hellothere.containers.socket.data.UserData;

import java.util.List;
import java.util.Objects;

public class ClientViewGroupModification extends Application {
    private Stage primaryStage;

    private boolean isRunning = false;

    public ObservableList<PrettyUserData> possibleMembers = FXCollections.observableArrayList();
    public ClientViewGroupModificationController cvgmc;

    public void setCvgcc(ClientViewGroupModificationController cvgmc) {
        this.cvgmc = cvgmc;
    }

    public void run() throws Exception {
        if(!isRunning) {
            start(new Stage());
            isRunning = true;
        }
        possibleMembers.clear();
        List<UserData> userDataList = ClientViewController.getAppView().getUserList("");
        for(UserData i : userDataList) possibleMembers.add(new PrettyUserData(i));
        cvgmc.getMembersList().setItems(possibleMembers);
        cvgmc.getMembersList().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        cvgmc.setGroupNameField(ClientViewController.getAppView().getCurrentGroup().getName());

        primaryStage.hide();
        primaryStage.show();
    }

    public void close() {
        if(primaryStage != null) primaryStage.close();
        isRunning = false;
    }

    Stage getPrimaryStage() { return this.primaryStage; }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GroupModification.fxml")));
        } catch (NullPointerException e) {
            throw new NoFxmlLoadedException();
        }

        primaryStage.setTitle("Hello There");
        primaryStage.setScene(new Scene(root, 350, 350));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(ClientViewController.getAppView().getPrimaryStage());

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class PrettyUserData {
        UserData data;

        PrettyUserData(UserData data) { this.data = data; }

        public UserData getData() { return data; }

        @Override
        public String toString() {
            return data.getName();
        }
    }

    public static class NoFxmlLoadedException extends Exception {}
}

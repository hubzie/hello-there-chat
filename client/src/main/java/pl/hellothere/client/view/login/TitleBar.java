package pl.hellothere.client.view.login;

import javafx.scene.layout.BorderPane;
import pl.hellothere.client.view.controller.ClientViewController;

public class TitleBar extends BorderPane {
    double startX = 0;
    double startY = 0;

    {
        setOnMousePressed(event -> {
            startX = event.getSceneX();
            startY = event.getSceneY();

        });
        setOnMouseDragged(event -> {
            ClientViewController.getLoginView().getPrimaryStage().setX(event.getScreenX() - startX);
            ClientViewController.getLoginView().getPrimaryStage().setY(event.getScreenY() - startY);
        });
    }
}

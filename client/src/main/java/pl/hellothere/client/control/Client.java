package pl.hellothere.client.control;

import javafx.application.Application;
import pl.hellothere.client.view.controller.ClientViewController;

public class Client {
    public static void main(String[] args) {
        ClientViewController.setCredentialsTest((a,b) -> (a.equals("General Kenobi")));
        Application.launch(ClientViewController.class);
    }
}
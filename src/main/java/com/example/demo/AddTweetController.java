package com.example.demo;

import Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AddTweetController {

    @FXML
    private TextField tweetTextField;
    @FXML
    private Label error;
    private boolean hasError;

    public void addNewTweet(ActionEvent event) throws IOException, ClassNotFoundException {
        hasError = false;
        String body = tweetTextField.getText();
        try {
            Client.addTweet(body);
        }
        catch (IllegalArgumentException | IOException | ClassNotFoundException | InterruptedException e){
            hasError = true;
            error.setText(e.getMessage());
        }
        if(!hasError) {
            MainPageController.switchToMainPage(event);
        }
    }
}

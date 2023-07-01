package com.example.demo;

import Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;

public class SignInController {

    @FXML
    private Parent root;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Label error;
    @FXML
    Button back;
    private Stage stage;
    private Scene scene;
    private boolean hasError = false;



    public void switchToMainMenu(ActionEvent event) { // exit button
        Button button = (Button) event.getSource();
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("FirstPage.fxml"));
        Parent root = null;
        try {
            root=loader.load();
        }catch (IOException e){
            System.out.println("KOMAK!");
        }
        Stage stage = (Stage) button.getScene().getWindow();
        Scene scene = null;
        if (root != null) {
            scene = new Scene(root);
        }
        stage.setScene(scene);
        stage.show();
    }



    public void signIn(ActionEvent event) throws IOException, ClassNotFoundException {
        String user, pass, feedback = " ";
        user = username.getText();
        pass = password.getText();
        hasError = false;
        try {
            feedback = Client.signIn(user,pass);
        }
        catch (IllegalArgumentException e){
            hasError = true;
            error.setText(e.getMessage());
        }
         catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (!feedback.equals("signed in successfully!")){
            error.setText(feedback);
            hasError = true;
        }
        if (!hasError){
            MainPageController.switchToMainPage(event);
        }
    }
}

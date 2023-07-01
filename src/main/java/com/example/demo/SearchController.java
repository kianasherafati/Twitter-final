package com.example.demo;

import Client.Client;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    public AnchorPane parent;
    @FXML
    private TextField searchChoice;
    @FXML
    private Button searchButton;
    @FXML
    private Label error;
    @FXML
    private Pane searchBox;
    @FXML
    private Label name;
    @FXML
    private Label username;
    @FXML
    private VBox searchOptions = new VBox();
    private User user;
    public void search(ActionEvent event) {
        try {
            String word = searchChoice.getText();
            Client.out.writeObject("4");
            Client.out.writeObject(word);
            ArrayList<User> foundUsers = (ArrayList<User>) Client.in.readObject();
            if (foundUsers.size() == 0) {
                error.setText("Not Found!");
            }
            showSearchOptions(foundUsers);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showSearch(User serverUser){
        name.setText(user.getFirstName());
        username.setText(user.getUsername());
    }

    public void showSearchOptions(ArrayList<User> serverUser){
        searchOptions.getChildren().clear();
        for (User user : serverUser){
            try {
                Node node = FXMLLoader.load(Client.class.getResource("BriefProfile.fxml"));
                ((SearchController) node.getUserData()).showSearch(user);
                searchOptions.getChildren().add(node);
            } catch (Exception ignore){
                ignore.printStackTrace();
            }
        }
    }


    public void backToMainPage(MouseEvent event) throws IOException, ClassNotFoundException {
        Client.out.writeObject("0");
        Parent root = FXMLLoader.load(Objects.requireNonNull(Client.class.getResource("MainPage.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        try {
            ((MainPageController) root.getUserData()).showTimeline(Client.timelineReceiver());
        } catch (NullPointerException e) {
            System.out.println("koomak !");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        parent.setUserData(this);
    }
}

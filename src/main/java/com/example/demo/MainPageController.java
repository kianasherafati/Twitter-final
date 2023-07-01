package com.example.demo;

import Client.Client;
import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
    public AnchorPane parent;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox timeline = new VBox();

    @FXML
    private ImageView profile;
    @FXML
    private ImageView search;
    @FXML
    private ImageView home;
    @FXML
    private Button textTweet;
    @FXML
    private ImageView photoTweet;
    private static Stage stage;
    private static Scene scene;
    private static ArrayList<Tweet> sentTweets;

    public void showTimeline(ArrayList<Tweet> serverTweets){
        sentTweets = serverTweets;
        for (int i=sentTweets.size()-1 ; i>=0 ; i--){
            try {
                Node node = FXMLLoader.load(Client.class.getResource("Tweet.fxml"));
                TweetController tweetController = (TweetController) node.getUserData();
                tweetController.showTweet(sentTweets.get(i));
                timeline.getChildren().add(node);
            } catch (Exception ignore){
                System.out.println("toye show timeline error dari");
            }
        }
    }

    public static void switchToMainPage(ActionEvent event) throws IOException, ClassNotFoundException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Client.class.getResource("MainPage.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
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

    public void switchToAddTweet(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("addTweet.fxml"));
        Parent root = null;
        try {
            root=loader.load();
        }catch (IOException e){
            System.out.println("KOMAK!");
        }
        Stage stage = (Stage) imageView.getScene().getWindow();
        Scene scene = null;
        if (root != null) {
            scene = new Scene(root);
        }
        stage.setScene(scene);
        stage.show();
    }
    public void switchToShowProfile(MouseEvent event) throws IOException, ClassNotFoundException {
        Client.out.writeObject("2");
        ImageView imageView = (ImageView) event.getSource();
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("showProfile.fxml"));
        Parent root = null;
        try {
            root=loader.load();
        }catch (IOException e){
            System.out.println("KOMAK!1");
        }
        Stage stage = (Stage) imageView.getScene().getWindow();
        Scene scene ;
        if (root != null) {
            Client.showMyProfile((ShowProfileController) root.getUserData());
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
    public void switchToSearch(MouseEvent event) throws IOException, ClassNotFoundException {
        ImageView imageView = (ImageView) event.getSource();
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("Search.fxml"));
        Parent root = null;
        try {
            root=loader.load();
        }catch (IOException e){
            System.out.println("KOMAK!1");
        }
        Stage stage = (Stage) imageView.getScene().getWindow();
        Scene scene ;
        if (root != null) {
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       parent.setUserData(this);
    }
}

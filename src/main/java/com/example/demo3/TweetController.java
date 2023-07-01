package com.example.demo3;


import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class TweetController implements Initializable {
    public AnchorPane parent;
    @FXML
    private Button profile;
    @FXML
    private ImageView like;
    @FXML
    private ImageView comment;
    @FXML
    private ImageView retweet;
    @FXML
    private ImageView quote;
    @FXML
    private Label commentCount;
    @FXML
    private Label retweetCount;
    @FXML
    private Label likeCount;
    @FXML
    private Label body;
    @FXML
    private Label name;
    @FXML
    private Label username;
    @FXML
    private Label passedTime;
    @FXML
    private Pane tweetBox;
    private Tweet tweet;

    public Pane showTweet(Tweet serverTweet){
        tweet = serverTweet;
        username.setText(tweet.getAuthor());
        tweetBox.getChildren().add(username);
        passedTime.setText(tweet.calculateTime());
        tweetBox.getChildren().add(passedTime);
        body.setText(tweet.getBody());
        tweetBox.getChildren().add(body);
        commentCount.setText(Integer.toString(tweet.getComments()));
        tweetBox.getChildren().add(commentCount);
        likeCount.setText(Integer.toString(tweet.getLikedUsers().size()));
        tweetBox.getChildren().add(likeCount);
        retweetCount.setText(Integer.toString(tweet.getRetweetUsers().size()));
        tweetBox.getChildren().add(retweetCount);
        tweetBox.getChildren().add(like);
        tweetBox.getChildren().add(comment);
        tweetBox.getChildren().add(retweet);
        tweetBox.getChildren().add(quote);
        return tweetBox;
    }



    public void like(MouseEvent event){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parent.setUserData(this);
    }
}

package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class Tweet implements Serializable {
    private String author;
    private String body;
    private int likes;
    private int retweets;
    private int comments;
    private boolean faveStar;
    private LocalDateTime tweetTime;
    String currentTime;
    private ArrayList<User> likedUsers = new ArrayList<>();
    private ArrayList<User> retweetUsers = new ArrayList<>();

    public String getBody() {
        return body;
    }

    public Tweet(String body, int likes, int retweets, int comments) {
        this.body = body;
        this.likes = likes;
        this.retweets = retweets;
        this.comments = comments;
        this.tweetTime = LocalDateTime.now();
        this.faveStar = false;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getRetweets() {
        return retweets;
    }

    public void setRetweets(int retweets) {
        this.retweets = retweets;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public boolean isFaveStar() {
        return faveStar;
    }

    public void setFaveStar(boolean faveStar) {
        this.faveStar = faveStar;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getAuthor() {
        return author;
    }

    public ArrayList<User> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(ArrayList<User> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public ArrayList<User> getRetweetUsers() {
        return retweetUsers;
    }

    public void setRetweetUsers(ArrayList<User> retweetUsers) {
        this.retweetUsers = retweetUsers;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        long diff = convertDate(tweetTime, LocalDateTime.now());
        if(0 < diff && diff < 60) {
            this.currentTime = "Just Now";
        }
        else if(0 < (diff/60) && (diff/60) < 60) {
            this.currentTime = (diff/60) + "m";
        }
        else if(0 < diff/3600 && diff/3600 <= 24) {
            this.currentTime = (diff/3600) + "h";
        }
        else {
            this.currentTime = (LocalDate.now().getDayOfMonth()) + " " + (LocalDate.now().getMonth());
        }
        return "*********************************" + "\n" +
                author+
                "\n"+
                body +
                "\nlikes : "+likes +
                "   retweets : " + retweets +
                "   comments : " + comments +
                "\n" + currentTime;
    }

    public static long convertDate(LocalDateTime startTime, LocalDateTime finishTime){
        ZoneId zoneId = ZoneId.systemDefault();
        Date start = Date.from(startTime.atZone(zoneId).toInstant());
        Date finish = Date.from(finishTime.atZone(zoneId).toInstant());
        return (finish.getTime() - start.getTime())/1000;
    }

    public String calculateTime(){
        long diff = convertDate(tweetTime, LocalDateTime.now());
        if(0 < diff && diff < 60) {
            this.currentTime = "Just Now";
        }
        else if(0 < (diff/60) && (diff/60) < 60) {
            this.currentTime = (diff/60) + "m";
        }
        else if(0 < diff/3600 && diff/3600 <= 24) {
            this.currentTime = (diff/3600) + "h";
        }
        else {
            this.currentTime = (LocalDate.now().getDayOfMonth()) + " " + (LocalDate.now().getMonth());
        }
        return currentTime;
    }
}

package Model;

import Server.ServerManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 6470817913146484535L;

    //ya email ya phoneNumber bayad bashe
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String country;
    private LocalDate birthDate;
    private LocalDate signupDate;
    private Date lastModificationDate;
    private boolean SignedUp;
    private boolean SignedIn;
    private boolean faveStar;
    private PersonalInfo personalInfo;
    private ArrayList<Tweet> tweets = new ArrayList<>();
    private ArrayList<User> followers = new ArrayList<>();
    private ArrayList<User> followings = new ArrayList<>();
    private ArrayList<User> blackList = new ArrayList<>();
    private ArrayList<Tweet> likedTweets = new ArrayList<>();
    private ArrayList<Tweet> retweetTweets = new ArrayList<>();

    public User(String username, String password, String firstName, String lastName, String email, String phoneNumber,
                String country, LocalDate birthDate) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.birthDate = birthDate;
        this.signupDate = LocalDate.now();
        this.SignedUp = false;
        this.SignedIn = false;
        this.personalInfo = new PersonalInfo("-", "-", "-");
    }

    public User copy(PersonalInfo newInfo) {
        User o = new User(username, password, firstName, lastName, email, phoneNumber, country, birthDate);
        o.personalInfo = newInfo;
        o.blackList = blackList;
        o.likedTweets = likedTweets;
        o.retweetTweets = retweetTweets;
        o.followings = followings;
        o.followers = followers;
        o.signupDate = signupDate;
        o.SignedIn = SignedIn;
        o.SignedUp = SignedUp;
        o.tweets = tweets;
        o.faveStar = faveStar;
        o.lastModificationDate = lastModificationDate;
        return o;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo =  personalInfo;
    }
    public void tweet(Tweet tweet) {
        ServerManager.getTweets().add(tweet);
        this.tweets.add(tweet);
    }

    public boolean checkSearch(User user, ObjectOutputStream out) {
        try {
            if (this.blackList.contains(user)) {
                out.writeObject("You have been blocked by this user");
                return false;
            }
            out.writeObject("success");
            return true;
        } catch (IOException e) {
            System.out.println("IO Exception in check search method");
            return false;
        }
    }

    public boolean checkFollow(User user, ObjectOutputStream out) {
        try {
            if (this.blackList.contains(user)) {
                out.writeObject("You have been blocked by this user");
                return false;
            }
            for (User followedUser : this.followings) {
                if (followedUser.equals(user)) {
                    out.writeObject("You have already followed this user!");
                    return false;
                }
            }
            out.writeObject("success");
        } catch (IOException e) {
            System.out.println("IO Exception in check follow method");
            return false;
        }
        return true;
    }
    public void follow(String tempUserUsername) {
        //this follows temp
        this.followings.add(ServerManager.getUsers().get(tempUserUsername));
        for(User user : ServerManager.getUsers().values()) {
            if(tempUserUsername.equals(user.getUsername())) {
                user.followers.add(this);
                return;
            }
        }
    }
    public boolean checkUnfollow(User user, ObjectOutputStream out) {
        try {
            if (this.blackList.contains(user)) {
                out.writeObject("You have been blocked by this user");
                return false;
            }
            for (User followedUser : followings) {
                if (followedUser.equals(user)) {
                    out.writeObject("success");
                    return true;
                }
            }
            out.writeObject("You haven't followed this user yet!");
            return false;
        } catch (IOException e) {
            System.out.println("IO Exception in check follow method");
            return false;
        }
    }
    public void unfollow(String tempUserUsername) {
        //this unfollows temp
        this.followings.remove(ServerManager.getUsers().get(tempUserUsername));
        for(User user : ServerManager.getUsers().values()) {
            if(tempUserUsername.equals(user.getUsername())) {
                user.followers.remove(this);
                return;
            }
        }
    }
    public boolean checkBlock(User user, ObjectOutputStream out) {
        try {
            for (User blockedUser : blackList) {
                if (blockedUser.getUsername().equals(user.getUsername())) {
                    out.writeObject("You have already blocked this user!");
                    return false;
                }
            }
            out.writeObject("success");
            return true;
        } catch (IOException e) {
            System.out.println("IO Exception in check block method");
            return true;
        }
    }

    public void block(String tempUserUsername) {
        //this blocks temp
        for(User user : this.followings) {
            if(tempUserUsername.equals(user.getUsername())) {
                this.followings.remove(user);
                break;
            }
        }
        for(User user : ServerManager.getUsers().get(tempUserUsername).getFollowers()) {
            if(this.getUsername().equals(user.getUsername())) {
                user.followers.remove(this);
            }
        }
        this.blackList.add(ServerManager.getUsers().get(tempUserUsername));
    }
    public boolean checkUnblock(User user, ObjectOutputStream out) {
        try {
            for (User blockedUser : blackList) {
                if (blockedUser.getUsername().equals(user.getUsername())) {
                    out.writeObject("success");
                    return true;
                }
            }
            out.writeObject("You haven't blocked this user yet!");
            return false;
        } catch (IOException e) {
            System.out.println("IO Exception in check block method");
            return true;
        }
    }
    public void unblock(String tempUserUsername) {
        //this unblocks temp
        this.blackList.remove(ServerManager.getUsers().get(tempUserUsername));
    }
    public ArrayList<Tweet> timeline() {
        ArrayList<Tweet> foundTweets = new ArrayList<>();
        for(Tweet tweet : ServerManager.getTweets()) {
        //  for(User user : this.followings) {
            //  if(tweet.getAuthor().equals(user.getUsername()) ||(tweet.isFaveStar() && !ServerManager.getUsers().get(tweet.getAuthor()).getBlackList().contains(ServerManager.getUsers().get(tweet.getAuthor())))) {
                  foundTweets.add(tweet);
              //}
          //}
        }
        return foundTweets;
    }
    public ArrayList<User> getFollowers() {
        return followers;
    }

    public ArrayList<User> getFollowings() {
        return followings;
    }

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(LocalDate signupDate) {
        this.signupDate = signupDate;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public boolean isSignedUp() {
        return SignedUp;
    }

    public void setSignedUp(boolean signedUp) {
        SignedUp = signedUp;
    }

    public boolean isSignedIn() {
        return SignedIn;
    }

    public void setSignedIn(boolean signedIn) {
        SignedIn = signedIn;
    }

    public boolean isFaveStar() {
        return faveStar;
    }

    public void setFaveStar(boolean faveStar) {
        this.faveStar = faveStar;
    }

    public void setFollowers(ArrayList<User> followers) {
        this.followers = followers;
    }

    public void setFollowings(ArrayList<User> followings) {
        this.followings = followings;
    }

    public ArrayList<User> getBlackList() {
        return blackList;
    }

    public void setBlackList(ArrayList<User> blackList) {
        this.blackList = blackList;
    }

    //For showing brief details in a list when you search for a user
    public String showSearchUser(){
        return this.firstName+" "+this.lastName+"\n"+this.username;
    }
}

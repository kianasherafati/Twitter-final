package Server;
import Model.PersonalInfo;
import Model.Tweet;
import Model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private User user;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch(IOException e) {
            //TODO: handle later
        }
    }

    protected void exit() {
        try {
            out.writeObject("bye :(");
            out.flush();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void run() {
        String userChoice;
        outer : while (true) {
            while (true) {
                for(Tweet tweet : ServerManager.getTweets()) {
                    System.out.println(tweet.getAuthor() + " : " + tweet.getBody());
                }
                try {
                    System.out.println("getting user's choice");
                    userChoice = (String) in.readObject();
                    if (userChoice.equals("3")) {
                        ServerManager.writeFile(ServerManager.getUsers());
                        exit();
                        break outer;
                    }
                    if (userChoice.equals("1")) {
                        if (ServerManager.getUsers().isEmpty()) ServerManager.readFile();
                        ServerManager.readTweetFile();
                        System.out.println("user is signing up...");
                        User tempUser = (User) in.readObject();
                        if (ServerManager.checkSignUp(tempUser, out)) {
                            ServerManager.signUp(tempUser);
                            user = tempUser;
                            System.out.println("user " + user.getUsername() + " signed up successfully :)");
                            out.writeObject("signed up successfully!");
                            while (true) {
                                String secondChoice = (String) in.readObject();
                                switch (secondChoice) {
                                    case "1" -> {
                                        System.out.println("showing timeline for " + user.getUsername());
//                                        out.writeObject(user.timeline());
                                        System.out.println("timeline SENT!");
                                    }
                                    case "2" -> { //show my profile
                                        out.writeObject(user);
                                        System.out.println("user info sent");
                                        while (true) {
                                            String profileChoice = (String) in.readObject();
                                            if(profileChoice.equals("1")) { //edit profile
                                                out.writeObject(user.getPersonalInfo());
                                                System.out.println(user.getUsername() + " pi is sent!");
                                                for(User aUser : ServerManager.getUsers().values()) {
                                                    if(aUser.getUsername().equals(user.getUsername())) {
                                                        PersonalInfo personalInfo = (PersonalInfo) in.readObject();
                                                        user = user.copy(personalInfo);
                                                        aUser.setPersonalInfo(personalInfo);
                                                    }
                                                }
                                            }
                                            else {
                                                break;
                                            }
                                        }
                                    }
                                    case "4" -> {
                                        String word = (String) in.readObject(); // receiving word to search
                                        if(!word.equals("0")) {
                                            out.writeObject(ServerManager.searchUser(word));
                                        }
                                    }
                                    case "5" -> {
                                        System.out.println(user.getUsername() + " is adding a tweet...");
                                        Tweet tweet = (Tweet) in.readObject();
                                        tweet.setAuthor(user.getUsername());
                                        user.tweet(tweet);
                                        System.out.println("user '" + user.getUsername() + "' added tweet successfully");
                                        out.writeObject("tweet added successfully!");
                                        ServerManager.writeTweetFile(ServerManager.getTweets());
                                        ServerManager.readTweetFile();
                                    }
                                    case "0" -> {
                                        break outer;
                                    }
                                }
                                ServerManager.writeFile(ServerManager.getUsers());
                                ServerManager.writeTweetFile(ServerManager.getTweets());
                            }
                        }
                    }
                    else if(userChoice.equals("2")) {
                        if (ServerManager.getUsers().isEmpty()) ServerManager.readFile();
                        ServerManager.readTweetFile();
                        System.out.println("user is signing in...");
                        User tempUser = (User) in.readObject();
                        if(ServerManager.checkSignIn(tempUser, out)) {
                            ServerManager.signIn(tempUser);
                            user = ServerManager.getUsers().get(tempUser.getUsername());
                            System.out.println("user '" + user.getUsername() + "' signed in successfully :)");
                            out.writeObject("signed in successfully!");
                            while (true) {
                                String secondChoice = (String) in.readObject();
                                switch (secondChoice) {
                                    case "1" -> {
                                        System.out.println("showing timeline for " + user.getUsername());
                                        System.out.println(user.timeline());
                                        out.writeObject(user.timeline());
                                        System.out.println("timeline SENT!");
                                    }
                                    case "2" -> {
                                        out.writeObject(user);
                                        System.out.println("user info sent");
                                        while (true) {
                                            String profileChoice = (String) in.readObject();
                                            if(profileChoice.equals("1")) { //edit profile
                                                out.writeObject(user.getPersonalInfo());
                                                System.out.println(user.getUsername() + " bio is : " + user.getPersonalInfo().getBio());
                                                System.out.println(user.getUsername() + " pi is sent!");
                                                PersonalInfo personalInfo = (PersonalInfo) in.readObject();
                                                for(User aUser : ServerManager.getUsers().values()) {
                                                    if(aUser.getUsername().equals(user.getUsername())) {
                                                        user = user.copy(personalInfo);
                                                        aUser.setPersonalInfo(personalInfo);
                                                        break;
                                                    }
                                                }
                                                out.writeObject(user);
                                                System.out.println(user.getUsername() + " bio is : " + user.getPersonalInfo().getBio());
                                                System.out.println("pi is sent again");
                                            }
                                            else {
                                                break;
                                            }
                                        }
                                    }
                                    case "4" -> {
                                        String word = (String) in.readObject(); // receiving word to search
                                        if(!word.equals("0")) {
                                            out.writeObject(ServerManager.searchUser(word));
                                        }
                                    }
                                    case "5" -> {
                                        System.out.println(user.getUsername() + " is adding a tweet...");
                                        Tweet tweet = (Tweet) in.readObject();
                                        tweet.setAuthor(user.getUsername());
                                        user.tweet(tweet);
                                        System.out.println("user '" + user.getUsername() + "' added tweet successfully");
                                        out.writeObject("tweet added successfully!");
                                        ServerManager.writeTweetFile(ServerManager.getTweets());
                                        ServerManager.readTweetFile();
                                    }
                                    case "0" -> {
                                        break outer;
                                    }
                                }
                                ServerManager.writeFile(ServerManager.getUsers());
                                ServerManager.writeTweetFile(ServerManager.getTweets());
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    try {
                        out.writeObject(e.getMessage());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (IOException e) {
                    //TODO: handle later
                   // e.printStackTrace();
                    break outer;
                } catch (ClassNotFoundException e) {
                    //TODO: handle later
                    e.printStackTrace();
                    //throw new RuntimeException(e);
                }
            }
//            while (true) {
//                try {
//                    System.out.println("getting user's choice after sign in or sign up :)");
//                    userChoice = (String) in.readObject();
//                    if(userChoice.equals("1")) {
//                        System.out.println("user is editing personal info...");
//                        out.writeObject(user.getPersonalInfo());
//                        out.writeObject(user.getFollowers().size());
//                        out.writeObject(user.getFollowings().size());
//                        System.out.println("user's personal info is sent :)");
//                        String answer = (String) in.readObject();
//                        if(answer.equals("1")) {
//                            String ifExit = (String) in.readObject();
//                            if(ifExit.equals("ok")) {
//                                PersonalInfo info = (PersonalInfo) in.readObject();
//                                user.setPersonalInfo(info);
//                                System.out.println("user '" + user.getUsername() + "' changed their personal info");
//                                out.writeObject("Personal info edited successfully!");
////                                ServerManager.writeFile(ServerManager.getUsers());
////                                ServerManager.readFile();
//                            }
//                        }
//                        else if(answer.equals("2")) {
//                            System.out.println("showing user's tweets...");
//                            out.writeObject(user.getTweets());
//                        }
//                        else if(answer.equals("3")) {
//                            System.out.println("showing user's black tweets...");
//                            out.writeObject(user.getBlackList());
//                        }
//                    }
//                    else if(userChoice.equals("2")) {
//                        System.out.println("user is searching...");
//                        String word = (String) in.readObject(); // receiving word to search
//                        if(!word.equals("exit")) {
//                            out.writeObject(ServerManager.searchUser(word)); //sending an arraylist of found users for client
//                            String ans = (String) in.readObject();
//                            if (ans.equals("ok")) {
//                                User chosenUser = (User) in.readObject();// receiving the chosen person
//                                out.writeObject(ServerManager.getUsers().get(chosenUser.getUsername()));//sending the user from DB (hash map :))
//                                if (user.checkSearch(chosenUser, out)) { //checks if user is blocked
//                                    String searchChoice = (String) in.readObject();
//                                    if (searchChoice.equals("1")) { //follow
//                                        User temp = (User) in.readObject();
//                                        User followedPerson = ServerManager.getUsers().get(temp.getUsername());
//                                        if (user.checkFollow(followedPerson, out)) {
//                                            user.follow(temp.getUsername());
//                                            out.writeObject(followedPerson.getUsername() + " is followed successfully");
//                                            System.out.println(temp.getUsername() + " is followed successfully :)");
////                                        ServerManager.writeFile(ServerManager.getUsers());
////                                        ServerManager.readFile();
//                                        }
//                                    } else if (searchChoice.equals("2")) { //unfollow
//                                        User temp = (User) in.readObject();
//                                        User unfollowedPerson = ServerManager.getUsers().get(temp.getUsername());
//                                        if (user.checkUnfollow(unfollowedPerson, out)) {
//                                            user.unfollow(temp.getUsername());
//                                            out.writeObject(temp.getUsername() + " is unfollowed successfully");
//                                            System.out.println(temp.getUsername() + " is unfollowed successfully :)");
////                                        ServerManager.writeFile(ServerManager.getUsers());
////                                        ServerManager.readFile();
//                                        }
//                                    } else if (searchChoice.equals("3")) { //block
//                                        User temp = (User) in.readObject();
//                                        User blockedPerson = ServerManager.getUsers().get(temp.getUsername());
//                                        if (user.checkBlock(blockedPerson, out)) {
//                                            user.block(blockedPerson.getUsername());
//                                            out.writeObject(blockedPerson.getUsername() + " is blocked successfully");
//                                            System.out.println(blockedPerson.getUsername() + " is blocked successfully");
////                                        ServerManager.writeFile(ServerManager.getUsers());
////                                        ServerManager.readFile();
//                                        }
//                                    } else if (searchChoice.equals("4")) { //unblock
//                                        User temp = (User) in.readObject();
//                                        User unblockedPerson = ServerManager.getUsers().get(temp.getUsername());
//                                        if (user.checkUnblock(unblockedPerson, out)) {
//                                            user.unblock(unblockedPerson.getUsername());
//                                            out.writeObject(unblockedPerson.getUsername() + " is unblocked successfully");
//                                            System.out.println(unblockedPerson.getUsername() + " is unblocked successfully");
////                                        ServerManager.writeFile(ServerManager.getUsers());
////                                        ServerManager.readFile();
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    else if(userChoice.equals("3")) {
//                        System.out.println("showing timeline for " + user.getUsername());
//                        out.writeObject(user.timeline());
//                     //   String ans = (String) in.readObject();
////                        if(ans.equals("ok")) {
//                            Tweet tempTweet = (Tweet) in.readObject();
//                            out.writeObject(ServerManager.getUsers().get(tempTweet.getAuthor()));
//                            String searchChoice = (String) in.readObject();
//                            if (searchChoice.equals("1")) { //follow
//                                User temp = (User) in.readObject();
//                                User followedPerson = ServerManager.getUsers().get(temp.getUsername());
//                                if (user.checkFollow(followedPerson, out)) {
//                                    user.follow(temp.getUsername());
//                                    out.writeObject(followedPerson.getUsername() + " is followed successfully");
//                                    System.out.println(temp.getUsername() + " is followed successfully :)");
////                                        ServerManager.writeFile(ServerManager.getUsers());
////                                        ServerManager.readFile();
//                                }
//                            } else if (searchChoice.equals("2")) { //unfollow
//                                User temp = (User) in.readObject();
//                                User unfollowedPerson = ServerManager.getUsers().get(temp.getUsername());
//                                if (user.checkUnfollow(unfollowedPerson, out)) {
//                                    user.unfollow(temp.getUsername());
//                                    out.writeObject(temp.getUsername() + " is unfollowed successfully");
//                                    System.out.println(temp.getUsername() + " is unfollowed successfully :)");
////                                        ServerManager.writeFile(ServerManager.getUsers());
////                                        ServerManager.readFile();
//                                }
//                            } else if (searchChoice.equals("3")) { //block
//                                User temp = (User) in.readObject();
//                                User blockedPerson = ServerManager.getUsers().get(temp.getUsername());
//                                if (user.checkBlock(blockedPerson, out)) {
//                                    user.block(blockedPerson.getUsername());
//                                    out.writeObject(blockedPerson.getUsername() + " is blocked successfully");
//                                    System.out.println(blockedPerson.getUsername() + " is blocked successfully");
////                                        ServerManager.writeFile(ServerManager.getUsers());
////                                        ServerManager.readFile();
//                                }
//                            } else if (searchChoice.equals("4")) { //unblock
//                                User temp = (User) in.readObject();
//                                User unblockedPerson = ServerManager.getUsers().get(temp.getUsername());
//                                if (user.checkUnblock(unblockedPerson, out)) {
//                                    user.unblock(unblockedPerson.getUsername());
//                                    out.writeObject(unblockedPerson.getUsername() + " is unblocked successfully");
//                                    System.out.println(unblockedPerson.getUsername() + " is unblocked successfully");
////                                        ServerManager.writeFile(ServerManager.getUsers());
////                                        ServerManager.readFile();
//                                }
//                            }
//                       // }
//                    }
//                    else if(userChoice.equals("4")) {
//                        System.out.println("user is adding a tweet...");
//                        String ifExit = (String) in.readObject();
//                        if(ifExit.equals("ok")) {
//                            Tweet tweet = (Tweet) in.readObject();
//                            tweet.setAuthor(user.getUsername());
//                            user.tweet(tweet);
//                            System.out.println("user '" + user.getUsername() + "' added tweet successfully");
//                            out.writeObject("tweet added successfully!");
//                            ServerManager.writeTweetFile(ServerManager.getTweets());
//                            ServerManager.readTweetFile();
//                        }
//                    }
//                    else if(userChoice.equals("5")) {
////                        ServerManager.writeFile(ServerManager.getUsers());
////                        ServerManager.readFile();
//                        break;
//                    }
//                } catch (ClassNotFoundException e) {
//                    System.out.println("class not found exception");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
}

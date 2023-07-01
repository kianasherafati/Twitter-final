package Server;

import Model.Tweet;
import Model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ServerManager {

    private static HashMap<String, User> users = new HashMap<>();
    private static ArrayList<Tweet> tweets = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public static void readFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("users.bin"))) {
            users = (HashMap<String, User>) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException | EOFException e) {
               System.out.println("No file found!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void readTweetFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("tweets.bin"))) {
            tweets = (ArrayList<Tweet>) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException | EOFException e) {
            System.out.println("No file found!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void writeFile(HashMap<String, User> users) {
       try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("users.bin"))) {
           objectOutputStream.writeObject(users);
       }
       catch(FileNotFoundException e ) {
           System.out.println("no users!");
       }
       catch(IOException e) {
           e.printStackTrace();
       }
    }
    public static void writeTweetFile(ArrayList<Tweet> tweets) {
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("tweets.bin"))) {
            objectOutputStream.writeObject(tweets);
        }
        catch(FileNotFoundException e ) {
            System.out.println("no tweets!");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    private static boolean checkUserNameDuplication(String userName) {
        return !users.containsKey(userName);
    }
    private static boolean checkEmailDuplication(String email) {
        for(User user : users.values()) {
            if(user.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }
    private static boolean checkPhoneNumberDuplication(String phoneNumber) {
        for(User user : users.values()) {
            if(user.getPhoneNumber().equals(phoneNumber)) {
                return false;
            }
        }
        return true;
    }
    private static boolean checkPreviousSignUp(User user) {
        for(User temp : users.values()) {
            if(temp.equals(user)) {
                return false;
            }
        }
        return true;
    }
    public static boolean checkSignUp(User user, ObjectOutputStream out) {
        try {
            if (!checkUserNameDuplication(user.getUsername())) {
                out.writeObject("This username is already taken!");
                return false;
            }
            if (user.getEmail() != null && !checkEmailDuplication(user.getEmail())) {
                out.writeObject("This email is already taken!");
                return false;
            }
            if (user.getPhoneNumber() != null && !checkPhoneNumberDuplication(user.getPhoneNumber())) {
                out.writeObject("This phone number is already taken!");
                return false;
            }
            if(!checkPreviousSignUp(user)) {
                out.writeObject("User has already signed up!");
                return false;
            }
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
            //TODO: HANDLE LATER
        }
    }
    public static void signUp(User user) throws IOException {
        users.put(user.getUsername(), user);
        writeFile(users);
    }

    public static  boolean checkPassword(String username, String password) {
        for(User user : users.values()) {
            if(user.getUsername().equals(username)) {
                if(user.getPassword().equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkSignIn(User user, ObjectOutputStream out) {
        try {
            if (checkUserNameDuplication(user.getUsername())) {
                out.writeObject("You haven't signed up yet!");
                return false;
            }
            if(!checkPassword(user.getUsername(), user.getPassword())) {
                out.writeObject("Wrong pass!");
                return false;
            }
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void signIn(User user) {
        for(User user1 : users.values()) {
            if(user.equals(user1)) {
                user1.setSignedIn(true);
            }
        }
    }
    public static ArrayList<User> searchUser(String word) {
        ArrayList<User> foundUsers = new ArrayList<>();
        System.out.println("We are searching for " + word);
        for(User user : users.values()) {
            if (user.getUsername().contains(word) || user.getFirstName().contains(word)) {
                foundUsers.add(user);
            }
        }
        System.out.println(foundUsers);
        return foundUsers;
    }
    public static HashMap<String, User> getUsers() {
        return users;
    }

    public static void setUsers(HashMap<String, User> users) {
        ServerManager.users = users;
    }

    public static ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public static void setTweets(ArrayList<Tweet> tweets) {
        ServerManager.tweets = tweets;
    }
}

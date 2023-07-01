package Client;

import Model.Tweet;
import Model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientManager {
    private static HashMap<String,String> countries = new HashMap<>();
    private static Scanner input = new Scanner(System.in);
    private static boolean flag = false;
    public static void showMenu() {
        System.out.println("""
                PLease choose an option :\s
                1.sign up
                2.sign in
                3.Exit""");
    }

    public static void showMainMenu(){
        System.out.print("""
                Please choose an option :\s
                1.Profile
                2.Search
                3.Timeline
                4.Tweet
                5.Exit
                """);
    }

    public static void showSearchMenu(){
        System.out.print("""
                Please choose an option :\s
                1.Follow
                2.Unfollow
                3.Block
                4.Unblock
                5.Exit
                """);
    }

    public static void showProfileMenu(){
        System.out.print("""
                Please choose an option :\s
                1.Edit personal info
                2.Show tweets
                3.Show blacklist
                4.Exit
                """);
    }


    public static boolean checkEmailFormat (String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean checkPhoneNumberLength(String phoneNumber) {
        return phoneNumber.length() == 11;
    }

    public static boolean checkPhoneNumberFormat(String phoneNumber) {
        return phoneNumber.startsWith("09");
    }

    public static boolean checkPasswordLength(String password) {
        return password.length() >= 8;
    }
    public static boolean checkPasswordFormat(String password) {
        return password.matches(".*[A-Z].*") && password.matches(".*[a-z].*");
    }
    public static void showCountries() {
        try(BufferedReader br = new BufferedReader(new FileReader("country.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(" ");
                countries.put(split[0],split[1]);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        for (String temp :countries.keySet()) {
            System.out.print( temp +" "+ countries.get(temp) + "\n" );
        }
    }
    public static String getCountry(String num){
        for (String temp : countries.keySet()) {
            if (num.equals(temp)){
                return countries.get(temp);
            }
        }
        return "try again";
    }


    public static void showTweet(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ArrayList<Tweet> tweets = new ArrayList<>();
        tweets = (ArrayList<Tweet>) in.readObject();
        if (tweets.size() == 0){
            System.out.println("No tweets!");
            return;
        }
        for (Tweet tweet : tweets){
            System.out.println(tweet.toString());
        }
    }

    public static void showBlacklist(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ArrayList<User> blacklists = new ArrayList<>();
        blacklists = (ArrayList<User>) in.readObject();
        if (blacklists.size() == 0){
            System.out.println("You haven't blocked anyone!");
        }
        else {
            System.out.println("Blacklist:");
            int i = 1;
            for (User user : blacklists){
                System.out.println(i + ". "+user.getUsername());
                i++;
            }
        }
    }

    public static User searchUser(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        User temp;
        System.out.println("Enter a username or firstname or lastname to find it:");
        System.out.println("write 'exit' in the field to exit:");
        String word = input.nextLine();
        out.writeObject(word);
        if (!word.equals("exit")) {
            ArrayList<User> foundUsers;
            HashMap<String, User> foundUsers2 = new HashMap<>();
            foundUsers = (ArrayList<User>) in.readObject();
            if (foundUsers.size() == 0) {
                System.out.println("Not Found!");
                out.writeObject("Not Found!");
            } else {
                out.writeObject("ok");
                for (int i = 0; i < foundUsers.size(); i++) {
                    foundUsers2.put(String.valueOf(i + 1), foundUsers.get(i));
                }
                int counter = 1;
                for (User user : foundUsers) {
                    System.out.println(counter + "- " + user.showSearchUser());
                    counter++;
                }
                System.out.println("Please choose a user:");
                System.out.println("write 'exit' in the field to exit:");
                String choice = input.nextLine();
                temp = foundUsers2.get(choice);
                out.writeObject(temp);
                User temp2;
                temp2 = (User) in.readObject();
                if (!choice.equals("exit")) {
                    return temp2;
                }
            }
        }
        return null;
    }
    public static void searchOptions(User user, ObjectOutputStream out, ObjectInputStream in){
        try {
            //user is the user who you want to follow or unfollow or block or unblock
            out.writeObject(user);
            //res is the answer sent from server that tells you if you can follow/unfollow user or not
            String res = in.readObject().toString();
            if(res.equals("success")) {
                System.out.println(in.readObject());
            }
            else {
                System.out.println(res);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    //For showing User's profile when you search and choose them
    public static void showProfile(User user, ObjectInputStream in) throws IOException, ClassNotFoundException {
        String condition = (String) in.readObject();
        //for checking if user is blocked or not
        if (condition.equals("success")) {
            System.out.println(user.getFirstName() + " " + user.getLastName() + "\n" +
                    user.getUsername() + "\n" + user.getPersonalInfo().toString() + "\n" +
                    "Followers : " + user.getFollowers().size() + "   " + "Following : " + user.getFollowings().size());
            if (user.getTweets().size() == 0) {
                System.out.println("No tweets!");
                return;
            }
            for (Tweet tweet : user.getTweets()) {
                System.out.println(tweet.toString());
            }
        }
        else {
            System.out.println(condition);
        }
    }

    public static User timeline(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        ArrayList<Tweet> tweets = new ArrayList<>();
        tweets = (ArrayList<Tweet>) in.readObject();
        if (tweets.size() == 0){
            System.out.println("No tweets found!!!");
            out.writeObject("No tweets found!!!");
            return null;
        }
        else {
            int cnt = 1;
            for (Tweet tweet : tweets) {
                System.out.println(cnt +". "+tweet.toString());
                cnt++;
            }
            System.out.println("choose a number(user): ");
            System.out.println("if you don't wanna see a profile write 'exit' :");
            String ans = input.nextLine();
            if (ans.equals("exit")){
                out.writeObject("exit");
                return null;
            }
            out.writeObject(tweets.get(Integer.parseInt(ans)-1));
            User temp = (User) in.readObject();
            out.writeObject("ok");
            return temp;
        }
    }
}

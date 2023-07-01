package Server;

import java.util.ArrayList;

public class Server extends Thread{

    static boolean running = true;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    public static void main(String[] args){
        ServerManager.readFile();
        ServerManager.readTweetFile();
        ServerThread serverThread = new ServerThread();
        serverThread.start();
    }

    public static ArrayList<ClientHandler> getClients() {
        return clients;
    }

    public static boolean isRunning() {
        return running;
    }
}


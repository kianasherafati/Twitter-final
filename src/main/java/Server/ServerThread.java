package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{

    private Socket socket;

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(9999);
            System.out.println("Server is running on port 9999");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (Server.isRunning()) {
            try {
                // accept a new client connection
                socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void shutDown() {
        for (ClientHandler clientHandler : Server.getClients()) {
            clientHandler.exit();
        }
    }
}

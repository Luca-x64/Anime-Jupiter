package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import config.Config;
import database.Database;

public class Server {
    private int counter = 0;


    //private List<Socket> connectedSockets = new ArrayList<>();
    private final Database DB = new Database();
    private ServerSocket serverSocket = null;

    public Server() {

        System.out.println("[SERVER ONLINE]");

        try  {
            serverSocket = Config.getServerSocket();
            serverSocket.setSoTimeout(120000); // 2 minuts timeout
            while (!serverSocket.isClosed()) {
                counter++;
                Socket socket = serverSocket.accept();
            // connectedSockets.add(socket);
                System.out.println("Socket connected!");

                Thread th = new Thread(new ServerThread(socket, DB));
                th.setName("ClientHandler-" + counter);
                th.start();
                System.out.println("[ClientHandler-" + counter + "] STARTED \n");
            }

            closeAll();
        } catch (SocketException e) {
            System.out.println("Socket Error! => " + e.getMessage());
            closeAll();
        } catch (IOException e) {
            System.out.println("IO Error => " + e.getMessage());
            closeAll();
        }
    }

    private void closeAll() {
        DB.closeConnection();
        System.out.println("Database connection closed!");
        try {
            serverSocket.close();
            System.out.println("Server socket closed!");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
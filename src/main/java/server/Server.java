package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import config.Config;
import database.Database;

public class Server{
    private int counter = 0;

    public Server() {
        final Database DB = new Database();
      
        try (ServerSocket serverSocket = new ServerSocket(Config.PORT)) {
            serverSocket.setSoTimeout(120000); // 2 minuts timeout

            while (!serverSocket.isClosed()) {
               counter++;
               Socket socket = serverSocket.accept();
               System.out.println("Socket connected!");
               Thread th = new Thread(new ServerThread(socket,DB));
               th.setName("ClientHandler-" + counter);
               th.start();
            }

            DB.closeConnection();
        } catch (SocketException e) {
           System.out.println("Socket Error! => " + e.getMessage());
           System.exit(4);
        } catch (IOException e) {
            System.out.println("IO Error => " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
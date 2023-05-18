package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import config.Config;
import database.Database;

public class Server{
    private int counter = 0;
    
    private final String outputFile = "Server.txt";

    private PrintStream psServerConsole;
    public Server() {
        try {
            psServerConsole = new PrintStream(outputFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
            psServerConsole.println("[SERVER ONLINE]");
            System.out.println("[SERVER ONLINE]");
        final Database DB = new Database();
      
        try (ServerSocket serverSocket = new ServerSocket(Config.PORT)) {
            serverSocket.setSoTimeout(120000); // 2 minuts timeout
            //serverSocket.setSoTimeout(1000000000); //CHECK remove

            while (!serverSocket.isClosed()) {
               counter++;
               Socket socket = serverSocket.accept();
               System.out.println("Socket connected!");
               Thread th = new Thread(new ServerThread(socket,DB));
               th.setName("ClientHandler-" + counter);
               psServerConsole.append("[ClientHandler-" + counter + "] STARTED \n");
               th.start();
            }

            DB.closeConnection();
            psServerConsole.append("Database connection closed!");
            serverSocket.close();
            psServerConsole.append("Server socket closed!");
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
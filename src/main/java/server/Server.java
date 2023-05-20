package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import config.Config;
import database.Database;

public class Server {
    private int counter = 0;

    private final String outputFile = "Server.txt";

    private PrintStream psServerConsole;
    private List<Socket> connectedSockets = new ArrayList<>();
    private final Database DB = new Database();
    private ServerSocket serverSocket = null;

    public Server() {
        try {
            psServerConsole = new PrintStream(outputFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        psServerConsole.println("[SERVER ONLINE]");
        System.out.println("[SERVER ONLINE]");

        try  {
            serverSocket = new ServerSocket(Config.PORT);
            serverSocket.setSoTimeout(120000); // 2 minuts timeout

            while (!serverSocket.isClosed()) {
                counter++;
                Socket socket = serverSocket.accept();
                connectedSockets.add(socket);
                System.out.println("Socket connected!");

                Thread th = new Thread(new ServerThread(socket, DB));
                th.setName("ClientHandler-" + counter);
                psServerConsole.append("[ClientHandler-" + counter + "] STARTED \n");
                th.start();
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
        psServerConsole.append("Database connection closed!");
        try {
            serverSocket.close();
            psServerConsole.append("Server socket closed!");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        for (Socket socket : connectedSockets) {
            try {
                socket.close();
            } catch (IOException e) {
               System.err.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
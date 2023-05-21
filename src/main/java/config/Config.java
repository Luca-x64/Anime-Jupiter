package config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.sound.sampled.Port;

import org.apache.logging.log4j.util.Chars;

import server.Server;

/**
 * CONF
 */
public class Config {

    public static String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static String DB_NAME = "anime_jupyter";
    public static int DB_PORT = 3306;

    public static String URL_CONNECTION = "jdbc:mysql://127.0.0.1:" + DB_PORT + "/" + DB_NAME;
    public static String USERNAME = "root";
    public static String PW = "";

    
    private static final String portPath = "src\\main\\resources\\port\\port.txt";
    private static final int DEFAULT_PORT = 20006;

    public static ServerSocket getServerSocket() {
        int port = getPort();
        ServerSocket serverSocket = null;
        boolean portAvailable = false;

        while (!portAvailable) {
            try {
                serverSocket = new ServerSocket(port);
                portAvailable = true;
            } catch (IOException e) {
                port++;
            }
        }

        return serverSocket;
    }

    public static int getPort() {
        int port = DEFAULT_PORT;

        try {
            if (Files.exists(Paths.get(portPath))) {
                String content = new String(Files.readAllBytes(Paths.get(portPath))).trim();
                if (!content.isEmpty()) {
                    port = Integer.parseInt(content);
                } else {
                    savePort(port);
                }
            } else {
                savePort(port);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return port;
    }

    public static void savePort(int port) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(portPath))) {
            writer.write(Integer.toString(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
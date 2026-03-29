package config;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * CONF
 */
public class Config {

    public static @NotNull String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static @NotNull String DB_NAME = "anime_jupyter";
    public static int DB_PORT = 3306;

    public static @NotNull String URL_CONNECTION = "jdbc:mysql://127.0.0.1:" + DB_PORT + "/" + DB_NAME;
    public static @NotNull String USERNAME = "root";
    public static @NotNull String PW = "";

    
    private static final @NotNull String portPath = "src/main/resources/port/port.txt";
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
            final Path path = Paths.get(portPath);
            if (Files.exists(path)) {
                final String content = new String(Files.readAllBytes(path)).trim();
                if (!content.isEmpty()) {
                    port = Integer.parseInt(content);
                } else {
                    savePort(port);
                }
            } else {
                savePort(port);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return port;
    }

    public static void savePort(int port) {
        try ( @NotNull BufferedWriter writer = new BufferedWriter(new FileWriter(portPath))) {
            writer.write(Integer.toString(port));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
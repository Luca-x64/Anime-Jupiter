package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;

import config.Config;

public class App extends Application implements Data {

    /**
     * Launch Applications
     *
     * @return void
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Socket socket;

    /**
     * Loading and showing the Start Window
     * 
     * @throws Exception
     */
    @Override
    public void start(Stage stage) {
        int cnt = 0;
        boolean connected = false;
        while (cnt < 3 && socket == null) {
            try {
                int port = Config.getPort();
                socket = new Socket(InetAddress.getLocalHost(), port);
                connected = true;
            } catch (Exception e) {
                cnt++;
                try {
                    Thread.sleep(3000);

                } catch (InterruptedException e1) {
                }
            }
        }
        if (connected) {
            try {

                FXMLLoader fxmlLoader = new FXMLLoader(
                        Objects.requireNonNull(getClass().getResource("/gui/login.fxml")));
                Parent root = fxmlLoader.load();

                interfaces.StreamController controller = fxmlLoader.getController();
                controller.setStream(new ObjectOutputStream(socket.getOutputStream()),
                        new ObjectInputStream(socket.getInputStream()));

                stage.setTitle(projectName);
                stage.getIcons().add(new Image(iconPath));
                stage.setScene(new Scene(root));
                stage.setResizable(true); // edited by luca on 22 may 21:00
                stage.show();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else {
            System.out.println("Not connected!");
            System.exit(7);
        }

    }

}

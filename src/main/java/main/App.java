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
import java.net.UnknownHostException;
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

        try {
            int port = Config.getPort();
            System.out.println(port);
            socket = new Socket(InetAddress.getLocalHost(), port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/gui/login.fxml")));
            Parent root = fxmlLoader.load();
           
            interfaces.StreamController controller = fxmlLoader.getController();
            controller.setStream(new ObjectOutputStream(socket.getOutputStream()) ,new ObjectInputStream(socket.getInputStream()));

            stage.setTitle(projectName);
            stage.getIcons().add(new Image(iconPath));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

}

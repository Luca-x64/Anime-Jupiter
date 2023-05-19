package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.ModuleLayer.Controller;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;

import javax.security.auth.login.LoginContext;

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

    /**
     * Loading and showing the Start Window
     * 
     * @throws Exception
     */
    @Override
    public void start(Stage stage) {

        Socket socket;
        try {
            socket = new Socket(InetAddress.getLocalHost(), Config.PORT);

            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/gui/login.fxml")));
            Parent root = fxmlLoader.load();
            interfaces.Controller controller = fxmlLoader.getController();
            controller.setSocket(socket);
            stage.setTitle(projectName);
            stage.getIcons().add(new Image(iconPath));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
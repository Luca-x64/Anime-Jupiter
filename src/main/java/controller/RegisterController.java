package controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import main.Data;
import model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import config.Config;

public class RegisterController implements interfaces.Controller, Initializable, Data {

    @FXML
    private Button registerBtn;
    @FXML
    private TextField inputName, inputEmail, inputPassword, inputCheckPassword;
    @FXML
    private AnchorPane anchorPane;

    private EventHandler<ActionEvent> loginAction;
    private Socket socket;
    private ObjectOutputStream os;
    private ObjectInputStream is;

    /**
     * Initialize
     * 
     * @param URL            url
     * @param ResourceBundle resourceBundle
     * @return void
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void register() throws IOException {
        disableButton();

        Boolean checkPassword = inputPassword.getText().equals(inputCheckPassword.getText());
        if (checkPassword) {
            User user = new User(inputName.getText(), inputEmail.getText(), inputPassword.getText());
            send(user);

            Boolean response = (Boolean) receive();
            send(response);
        } else {
            this.registerBtn.setOnAction(loginAction);
        }

        gotoLogin();

    }

    //TODO controllare, non funziona
    private void gotoLogin() { 
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/gui/register.fxml")));
            Parent root = fxmlLoader.load();
            interfaces.Controller controller = fxmlLoader.getController();
            controller.setSocket(socket);
            Scene loginScene = registerBtn.getScene();
            root.translateYProperty().set(loginScene.getHeight());
            anchorPane.getChildren().add(root);

            // CHECK (idk)
            // stage.setTitle(projectName);
            // stage.getIcons().add(new Image(iconPath));
            // stage.setScene(new Scene(root));
            // stage.setResizable(false);
            // stage.show();

            // Transition

            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(t -> anchorPane.getChildren().remove(anchorPane));
            timeline.play();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void send(Object o) {
        try {
            os.writeObject(o);
            os.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Object receive() {
        Object received = null;
        try {
            received = is.readObject();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return received;
    }

    // /**
    // * Admin side
    // *
    // * @throws IOException
    // * @return void
    // */
    // public void adminSide() throws IOException {

    // FXMLLoader fxmlLoader = new
    // FXMLLoader(Objects.requireNonNull(getClass().getResource(guiAdmin)));
    // Parent root = fxmlLoader.load();
    // AdminController ac = fxmlLoader.getController();
    // ac.setAc(ac);

    // Scene adminScene = registerBtn.getScene();
    // root.translateYProperty().set(adminScene.getHeight());
    // anchorPane.getChildren().add(root);

    // // Transition

    // Timeline timeline = new Timeline();
    // KeyValue kv = new KeyValue(root.translateYProperty(), 0,
    // Interpolator.EASE_IN);
    // KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
    // timeline.getKeyFrames().add(kf);
    // timeline.setOnFinished(t -> anchorPane.getChildren().remove(anchorPane));
    // timeline.play();
    // }

    // /**
    // * User Side
    // *
    // * @throws IOException
    // * @return void
    // */
    // public void userSide() throws IOException {
    // disableButton();

    // Parent root =
    // FXMLLoader.load((Objects.requireNonNull(getClass().getResource(guiUser))));
    // Scene userScene = registerBtn.getScene();
    // root.translateYProperty().set(userScene.getHeight());
    // anchorPane.getChildren().add(root);

    // // Transition
    // Timeline timeline = new Timeline();
    // KeyValue kv = new KeyValue(root.translateYProperty(), 0,
    // Interpolator.EASE_IN);
    // KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
    // timeline.getKeyFrames().add(kf);
    // timeline.setOnFinished(t -> anchorPane.getChildren().remove(anchorPane));
    // timeline.play();
    // }

    /**
     * Disable user and admin side Buttons
     *
     * @return void
     */
    private void disableButton() {
        loginAction = this.registerBtn.getOnAction();
        this.registerBtn.setOnAction(null);
    }

    public void setSocket(Socket s) {
        this.socket = s;
        try {
            os = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
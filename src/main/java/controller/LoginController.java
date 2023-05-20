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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import main.Data;
import model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.mysql.cj.protocol.SocketConnection;

import interfaces.StreamController;



public class LoginController implements interfaces.SocketController, Initializable, Data {

    @FXML
    private Button loginBtn;
    @FXML
    private TextField inputEmail, inputPassword;
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
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void login() throws IOException {
        disableButton();

        User user = new User(inputEmail.getText(),inputPassword.getText());
        send(user);

        // TODO continuare sotto

        Boolean emailVerify = (Boolean) receive();

        if (emailVerify) {
            Boolean isLogged = (Boolean) receive();
            if (isLogged) {
                boolean isAdmin = (Boolean) receive();
                if (isAdmin) {
                    adminSide();
                } else {
                    userSide();
                }
            } else {
                System.out.println("Wrong password!");
                this.loginBtn.setOnAction(loginAction);
            }

        } else {
            System.out.println("Wrong email!");
            this.loginBtn.setOnAction(loginAction);
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

    /**
     * Admin side
     * 
     * @throws IOException
     * @return void
     */
    public void adminSide() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(guiAdmin)));
        Parent root = fxmlLoader.load();

        AdminController ac = fxmlLoader.getController(); //TODO CHECK these 2 lines
        ac.setAc(ac);

        ac.setStream(os,is); 


        Scene adminScene = loginBtn.getScene();
        root.translateYProperty().set(adminScene.getHeight());
        anchorPane.getChildren().add(root);

        // Transition

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> anchorPane.getChildren().remove(anchorPane));
        timeline.play();
    }

    /**
     * User Side
     *
     * @throws IOException
     * @return void
     */
    public void userSide() throws IOException {
        disableButton();

        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(guiUser)));
        Parent root = fxmlLoader.load();

        UserController uc = fxmlLoader.getController();
        uc.setStream(os,is);

        Scene userScene = loginBtn.getScene();
        root.translateYProperty().set(userScene.getHeight());
        anchorPane.getChildren().add(root);

        // Transition
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> anchorPane.getChildren().remove(anchorPane));
        timeline.play();
    }

    /**
     * Disable user and admin side Buttons
     *
     * @return void
     */
    private void disableButton() {
        loginAction = this.loginBtn.getOnAction();
        this.loginBtn.setOnAction(null);
    }

    @Override
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
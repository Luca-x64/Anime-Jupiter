package controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import main.Data;
import model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import interfaces.StreamController;



public class LoginController implements interfaces.StreamController, Initializable, Data {

    @FXML
    private Label loginBtn;
    @FXML
    private TextField inputEmail, inputPassword;
    @FXML
    private AnchorPane anchorPane;

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

    @FXML
    void changeToRegister(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((Objects.requireNonNull(getClass().getResource("/gui/register.fxml"))));
            Parent root = fxmlLoader.load();
            StreamController sc = (fxmlLoader.getController());
            sc.setStream(os, is);
            Scene scene = loginBtn.getScene();
            root.translateYProperty().set(scene.getHeight());
            anchorPane.getChildren().add(root);
    
            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(t -> anchorPane.getChildren().remove(anchorPane));
            timeline.play();
        } catch (Exception e) {
            // TODO: handle exception
        }
       
    }


    public void login(MouseEvent event) {
        loginBtn.setDisable(true);

        User user = new User(inputEmail.getText(),inputPassword.getText());
        send(user);


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
                loginBtn.setDisable(false);
            }

        } else {
            System.out.println("Wrong email!");
            loginBtn.setDisable(false);
        }

    }

    private synchronized void send(Object o) {
        try {
            os.writeObject(o);
            os.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private synchronized Object receive() {
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
    public void adminSide() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(guiAdmin)));
            Parent root = fxmlLoader.load();
    
            AdminController ac = fxmlLoader.getController();
            ac.setStream(os,is); 
            ac.begin();
    
    
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
        } catch (Exception e) {
            // TODO: handle exception
        }
      
    }

    /**
     * User Side
     *
     * @throws IOException
     * @return void
     */
    public void userSide() {
        try {
            loginBtn.setDisable(true);

            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(guiUser)));
            Parent root = fxmlLoader.load();
    
            UserController uc = fxmlLoader.getController();
            uc.setStream(os,is);
            uc.begin();
                
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
        } catch (Exception e) {
            // TODO: handle exception
        }
      
    }

    @Override
    public void setStream(ObjectOutputStream os, ObjectInputStream is) {
        this.os=os;
        this.is=is;
    }
   
}
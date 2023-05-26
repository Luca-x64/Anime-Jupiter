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
import javafx.scene.input.MouseEvent;
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

import interfaces.StreamController;

public class RegisterController implements interfaces.StreamController, Initializable, Data {

    @FXML
    private Label registerBtn;
    @FXML
    private TextField inputName, inputEmail, inputPassword, inputCheckPassword;
    @FXML
    private AnchorPane anchorPane;

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

    @FXML
    void backToLogin(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((Objects.requireNonNull(getClass().getResource("/gui/login.fxml"))));
            Parent root = fxmlLoader.load();
            StreamController sc = (fxmlLoader.getController());
            sc.setStream(os, is);
            Scene scene = registerBtn.getScene();
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

    @FXML
    void register(MouseEvent event) {

        registerBtn.setDisable(true);

        Boolean checkPassword = inputPassword.getText().equals(inputCheckPassword.getText());
        if (checkPassword) {
            User user = new User(inputName.getText(), inputEmail.getText(), inputPassword.getText());
            send(user);

            Boolean response = (Boolean) receive();
            if (response) { // TODO
                send(new User(user.getEmail(), user.getPassword()));
                boolean isAdmin = (Boolean) receive();
                if (isAdmin) {
                    adminSide();
                } else {
                    userSide();
                }

           
        } else {
            System.out.println("Cant create your account!");
        }
    }else

    {
        registerBtn.setDisable(false);
    }

    // gotoLogin();

    }

    // TODO controllare, non funziona
    private void gotoLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/gui/login.fxml")));
            Parent root = fxmlLoader.load();
            interfaces.StreamController controller = fxmlLoader.getController();
            controller.setStream(os, is);
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
    /**
     * Admin side
     * 
     * @throws IOException
     * @return void
     */
    public void adminSide() {
        try {
            registerBtn.setDisable(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(guiAdmin)));
            Parent root = fxmlLoader.load();
    
            AdminController ac = fxmlLoader.getController();
            ac.setStream(os,is); 
            ac.begin();
    
    
            Scene adminScene = registerBtn.getScene();
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
            System.err.println("cant load admin panel");
            System.exit(4);
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
            registerBtn.setDisable(true);

            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(guiUser)));
            Parent root = fxmlLoader.load();
    
            UserController uc = fxmlLoader.getController();
            uc.setStream(os,is);
            uc.begin();
                
            Scene userScene = registerBtn.getScene();
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
            System.err.println("cant load admin panel");
            System.exit(5);
        }
      
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

    @Override
    public void setStream(ObjectOutputStream os, ObjectInputStream is) {
        this.os = os;
        this.is = is;
    }

}
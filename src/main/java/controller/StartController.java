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
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import main.Data;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
public class StartController implements Initializable, Data {

    @FXML
    private Button adminBtn, userBtn;
    @FXML
    private AnchorPane anchorPane;

    /**
     * Initialize
     * 
     * @param URL url
     * @param ResourceBundle resourceBundle
     * @return void
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Admin side
     * 
     * @throws IOException
     * @return void
     */
    public void adminSide() throws IOException {
        disableButtons();

        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(guiAdmin)));
        Parent root = fxmlLoader.load();
        AdminController ac = fxmlLoader.getController();
        ac.setAc(ac);

        Scene adminScene = adminBtn.getScene();
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
        disableButtons();

        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource(guiUser))));
        Scene userScene = userBtn.getScene();
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
    private void disableButtons(){
        this.userBtn.setOnAction(null);
        this.adminBtn.setOnAction(null);
    }
}
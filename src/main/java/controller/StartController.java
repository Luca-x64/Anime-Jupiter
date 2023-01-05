package controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.Objects;

import app.Data;

public class StartController {

    @FXML
    private Button adminBtn, userBtn;
    @FXML
    private AnchorPane anchorPane;
    private EventHandler<ActionEvent> userAction;
    private EventHandler<ActionEvent> adminAction;

    /**
     * Admin side
     * 
     * @return void
     */
    public void adminSide() {
        disableButtons();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(Data.guiAdmin)));
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
        } catch (Exception e) {
            enableButtons();
        }

    }

    /**
     * User Side
     *
     * @return void
     */
    public void userSide() {
        disableButtons();
        try {
            Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource(Data.guiUser))));
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
        } catch (Exception e) {
            enableButtons();
        }

    }

    /**
     * Disable user and admin Buttons
     *
     * @return void
     */
    private void disableButtons() {
        userAction = this.userBtn.getOnAction();
        adminAction = this.adminBtn.getOnAction();
        this.userBtn.setOnAction(null);
        this.adminBtn.setOnAction(null);
    }
    
    /**
     * Enable user and admin Buttons
     *
     * @return void
     */
    private void enableButtons() {
        this.userBtn.setOnAction(userAction);
        this.adminBtn.setOnAction(adminAction);
    }
    
}
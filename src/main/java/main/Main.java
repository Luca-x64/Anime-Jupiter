package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application implements Data {

    /** Main
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /** Loading and showing the Start Window
     *
     * @param stage stage
     * @throws Exception exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(guiStart)));
        stage.setTitle(projectName);
        stage.getIcons().add(new Image(iconProjectFolder));
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

}
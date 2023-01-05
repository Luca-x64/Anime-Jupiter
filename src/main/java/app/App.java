package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application{

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
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Data.guiStart)));
        stage.setTitle(Data.projectName);
        stage.getIcons().add(new Image(Data.iconPath));
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

}
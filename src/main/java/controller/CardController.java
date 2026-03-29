package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import main.Data;
import main.Listener;
import model.Anime;

import java.io.File;
import java.io.FileInputStream;

public class CardController implements Data {

    @FXML
    private Label title;
    @FXML
    private ImageView img;
    @FXML
    private AnchorPane IdAnchorPane;
    private Anime anime;
    private Listener listener;

    /**
     * Click Listener
     * 
     * @return void
     */
    @FXML
    private void click() {
        listener.onClickListener(anime);
    }

    /**
     * Set Data
     * 
     * @param anime
     * @param listener
     * @return void
     */
    public void setData(Anime anime, Listener listener) {
        this.anime = anime;
        this.listener = listener;
        IdAnchorPane.setId(String.valueOf(anime.getID()));
        title.setText(anime.getTitle());

        try {
            File file = new File(anime.getImagePath());
            FileInputStream fIStream = new FileInputStream(file);
            Image image = new Image(fIStream);
            img.setImage(image);
        } catch (Exception e) {
            img.setImage(new Image(imgDefaultRelPath));
        }

        Rectangle rect = new Rectangle();
        rect.setWidth(220);
        rect.setHeight(124);
        rect.setArcHeight(30);
        rect.setArcWidth(30);
        img.setPreserveRatio(true);
        img.setClip(rect);
    }
}
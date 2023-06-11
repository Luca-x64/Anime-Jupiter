package controller;

import engine.Engine;
import interfaces.StreamController;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.Listener;
import model.Anime;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class UserController extends Engine implements Initializable {
    @FXML
    private VBox chosenAnime;
    @FXML
    private ImageView animeImg, heart;

    @FXML
    private Label animeTitle, ttlAnime, ttlJupiter;
    @FXML
    private TextArea animeData;
    @FXML
    private TextField inputBox;
    @FXML
    private GridPane grid;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button sortButton;
    @FXML
    private HBox testoScroll;
    private Anime selectedAnime;
    private TranslateTransition tt;
    private Timer timer;
    private boolean longMessagge = false;
    private int cnt = 0, cnt2 = 0;

    /**
     * Initialize
     * 
     * @param URL            url
     * @param ResourceBundle resourceBundle
     * @return void
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // reload(getAnimeList()); UPDATING PROCESS CHECK
    }

    /**
     * Sort loaded anime
     * 
     * @return void
     */
    @FXML
    void sort() {
        inputBox.setText(empty);
        switch (cnt) {
            case 0 -> {
                reload(sortTitle(true));
                sortButton.setText(orderAlpha);
            }
            case 1 -> {
                reload(sortTitle(false));
                sortButton.setText(reversedAlpha);
            }
            case 2 -> {
                reload(sortYear(false));
                sortButton.setText(orderYear);
            }
            case 3 -> {
                reload(sortYear(true));
                sortButton.setText(reversedYear);
            }
            default -> {
                reload(getAnimeList());
                sortButton.setText(sort);
                cnt = -1;
            }
        }
        cnt++;
    }

    @FXML
    void favouriteSort(MouseEvent event) {

        List<Anime> f = getSuperFavourite();
        if(f.size()>0){
            reload(f);
        } else {
            scrollingText(red, msgDanger(noAnime));
        }
        
    }


    /**
     * Search Button Click
     * 
     * @return void
     */
    @FXML
    void searchPress() {
        resetScroll();

        String input = stringFormat(inputBox.getText());
        if (input.length() > 0) {
            List<Anime> resultQuery = query(input);
            if (resultQuery.size() > 0) {
                reload(resultQuery);
            } else {
                reload(new ArrayList<>());
                scrollingText(red, msgDanger(noAnime));
            }
        } else {
            reload(getAnimeList());
            resetScroll();
        }
    }

    /**
     * Scrolling text
     *
     * @param Color  text color
     * @param String text content
     * @return void
     */
    public void scrollingText(Color color, String text) {

        if (cnt2 != 0) {
            resetScroll();
        }

        Text scrollingText = new Text(text);
        testoScroll.getChildren().add(scrollingText);
        testoScroll.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        scrollingText.setEffect(glow);
        scrollingText.setStyle(scrollingTextFX);
        scrollingText.setFill(color);
        scrollingText.setLayoutX(0);
        scrollingText.setLayoutY(0);
        scrollingText.setWrappingWidth(0);

        int time = text.length() / 4 * 1000 + 1000;
        tt = new TranslateTransition(Duration.millis(time), scrollingText);

        if (longMessagge) {
            tt.setFromX(0 - scrollingText.getWrappingWidth() - 410);
            tt.setToX(scrollingText.getWrappingWidth() + 30);
        } else {
            tt.setFromX(0 - scrollingText.getWrappingWidth() - 350);
            tt.setToX(scrollingText.getWrappingWidth() + 50);
        }

        longMessagge = false;
        tt.setCycleCount(1);
        tt.setAutoReverse(false);
        tt.play();
        cnt2++;
        runLater(time);
    }

    /**
     * Reset Scrolling text
     *
     * @return void
     */
    private void resetScroll() {
        try {
            testoScroll.getChildren().clear();
            tt.stop();
            timer.purge();
            timer.cancel();
        } catch (Exception ignored) {
        }
    }

    /**
     * Run later with time
     *
     * @param int duration time
     * @return void
     */
    public void runLater(int time) {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> testoScroll.getChildren().clear());
            }
        };
        time *= 2;
        timer.schedule(timerTask, time, time);
    }

    /**
     * Back to login window
     *
     * @throws IOException Exception
     * @return void
     */
    @FXML
    void backToLogin() throws IOException {
        logout();
        ttlJupiter.setOnMouseClicked(null);
        ttlAnime.setOnMouseClicked(null);

        FXMLLoader fxmlLoader = new FXMLLoader((Objects.requireNonNull(getClass().getResource("/gui/login.fxml"))));
        Parent root = fxmlLoader.load();
        setLowerStream(fxmlLoader.getController());
        Scene scene = ttlAnime.getScene();
        root.translateYProperty().set(scene.getHeight());
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
     * Set Chosen Anime
     *
     * @param Anime chosen anime
     * @return void
     */
    private void setChosenAnime(Anime anime) {
        if (anime == null) {
            this.selectedAnime = null;
            animeTitle.setText(empty);
            animeData.setText(empty);
            animeImg.setImage(new Image(imgDefaultRelPath));
            chosenAnime.setStyle(chosenAnimeFX);
        } else {
            this.selectedAnime = anime;
            animeTitle.setText(anime.getTitle());
            animeData.setText(anime.toString());
            try {
                animeImg.setImage(loadImage(anime.getImagePath()));
            } catch (Exception ignored) {
                animeImg.setImage(new Image(imgDefaultRelPath));
            }
            chosenAnime.setStyle(chosenAnimeFX);
        }
    }

    /**
     * Reload Grid panel (anime)
     *
     * @param List<Anime> anime list
     * @return void
     */
    public void reload(List<Anime> al) {
        grid.getChildren().clear();

        if (al.size() > 0) { // todo dinamico della posizione ??
            setChosenAnime(al.get(0));
            this.selectedAnime = al.get(0);
        } else {
            setChosenAnime(null);
        }

        Listener listener = this::setChosenAnime;
        int column = 3;
        int row = 1;
        try {
            for (Anime a : al) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(guiCard));
                AnchorPane anchorPane = fxmlLoader.load();

                CardController cardController = fxmlLoader.getController();
                cardController.setData(a, listener);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);
                // Set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                // Set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(8));
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Open CrunchyRoll link
     * 
     * @return void
     */
    public void crunchyRollLink() {
        openLink(crunchyRollLink);
    }

    protected void getFavourite(){
        List<Anime> favouriteAnime = getSuperFavourite();
        reload(favouriteAnime);
    }

    // @FXML
    public void addFavourite() {
        
        
        boolean r = updateFavourite(selectedAnime.getID());
        System.out.println("status update favourite: "+ r);
        if (r) {
            String percorsoImmagine = "/project/empty_heart.png";
            System.out.println("update favourite status");
            
            // TODO @J7044
        // Carica l'immagine dal percorso specificato

        //percorsoImmagine = selectedAnime.favourite()?"/project/empty_heart.png":"/project/empty_heart.png";
//
        //Image nuovaImmagine = new Image(getClass().getResourceAsStream(percorsoImmagine));
        //heart.setImage(nuovaImmagine);
    }

    }

    /**
     * Open Link Anime
     *
     * @return void
     */
    @FXML
    void linkAnime() {
        System.out.println("link: " + selectedAnime.getLink()); // DEBUG
        openLink(selectedAnime.getLink());
    }

    /**
     * Enter press
     *
     * @param KeyEvent keyEvent keyboard press
     * @return void
     */
    public void pressEnter(javafx.scene.input.KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            searchPress();
    }

    public void begin() {
        receiveAllAnime();
        reload(getAnimeList());
    }

}
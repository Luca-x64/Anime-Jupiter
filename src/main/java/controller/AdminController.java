package controller;

import engine.Engine;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Listener;
import model.Anime;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AdminController extends Engine implements Initializable {
    @FXML
    private ImageView animeImg, animeDelete, animeEdit;
    @FXML
    private Label animeTitle, ttlAnime,ttlJupiter;
    @FXML
    private TextArea animeData;
    @FXML
    private VBox chosenAnime;
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
    private AdminController ac;
    public Stage addAnimeStage;
    public Stage editAnimeStage;
    private Anime selectedAnime;
    protected TranslateTransition tt;
    protected Timer timer;
    protected TimerTask timerTask;
    private int cnt=0,cnt2=0;
    private boolean addAnimeActive = false;
    private boolean editAnimeActive = false;
    private boolean LongMessagge = false;

    EventHandler<MouseEvent> editHandler = mouseEvent -> { try { editAnime(); } catch (IOException ignored) {System.out.println(ignored);} };
    EventHandler<MouseEvent> deleteHandler = mouseEvent -> deleteAnime();
    
    /**
     * Initialize
     * 
     * @param URL url
     * @param ResourceBundle resourceBundle
     * @return void
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reload(getAnimeList());
    }

    /**
     * Sort loaded anime
     * 
     * @return void
     */
     @FXML
     void sort(){
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

    /**
     * Back to start
     * 
     * @throws IOException
     * @return void
     */
    @FXML
    void backToStart() throws IOException {
        ttlJupiter.setOnMouseClicked(null);
        ttlAnime.setOnMouseClicked(null);
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource(guiStart))));
        Scene scene = ttlAnime.getScene();
        root.translateYProperty().set(scene.getHeight());
        anchorPane.getChildren().add(root);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> anchorPane.getChildren().remove(anchorPane));
        timeline.play();
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
            if (resultQuery.size() > 0) reload(resultQuery);
            else {
                reload(new ArrayList<>());
                scrollingText(red,msgDanger(noAnime));
            }
        } else {
            reload(getAnimeList());
            resetScroll();
        }
    }

    /**
     * Reload grid panel (anime)
     * 
     * @param List<Anime> al anime list
     * @return void
     */
    public void reload(List<Anime> al) {
        grid.getChildren().clear();
        if (al.size() > 0) { //todo dinamico della posizione
            setChosenAnime(al.get(0));
            this.selectedAnime = al.get(0);
        } else setChosenAnime(null);

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
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(8));
            }
        } catch (Exception ignored) {}
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
            chosenAnime.setStyle(chosenAnimeFX); //sposta da qui
            animeDelete.setOnMouseClicked(null);
            animeEdit.setOnMouseClicked(null);
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
            animeDelete.setOnMouseClicked(deleteHandler);
            animeEdit.setOnMouseClicked(editHandler);
        }
    }


    /**
     * Add anime
     * 
     * @throws IOException
     * @return void
     */
    @FXML
    void addAnimeclick() throws IOException {
        if(!addAnimeActive){
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(guiAddAnime)));
            Parent root = fxmlLoader.load();
            AddAnimeController aac = fxmlLoader.getController();
            aac.setAc(ac);

            addAnimeStage = new Stage();
            addAnimeStage.getIcons().add(new Image(iconPath));
            addAnimeStage.setTitle(addAnime);
            addAnimeStage.setScene(new Scene(root));
            addAnimeStage.setAlwaysOnTop(true);
            addAnimeStage.initModality(Modality.APPLICATION_MODAL);
            addAnimeStage.setOnCloseRequest(windowEvent -> addClose());
            addAnimeStage.setResizable(false);
            addAnimeStage.show();

            addAnimeActive = true;
            scrollingText(yellow, msgWarning(addingAnime));
        }
    }

    /**
     * Delete Anime 
     * 
     * @return void
     */
    @FXML
    void deleteAnime() {

        int pos = getAnimeList().indexOf(selectedAnime);
        deleteInFile(pos);

        List<Anime> alCopy = new ArrayList<>(ac.getAnimeList());
        alCopy.remove(selectedAnime);
        setAnimeList(alCopy);

        grid.getChildren().clear();
        reload(getAnimeList());
        if (getAnimeList().size() == 1) {
            this.selectedAnime = getAnimeList().get(0);
            setChosenAnime(getAnimeList().get(0));
        } else if(getAnimeList().size() >1){
            this.selectedAnime = getAnimeList().get(pos-1);
            setChosenAnime(getAnimeList().get(pos-1));
        }
        else {
            this.selectedAnime = null;
            setChosenAnime(null);
        }
        scrollingText(red, msgDanger(animeDeleted));
    }

     /**
     * Edit Anime
     * 
     * @throws IOException
     * @return void
     */
    @FXML
    void editAnime() throws IOException {
        if(!editAnimeActive){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Objects.requireNonNull(getClass().getResource(guiEditAnime)));
            Parent root = fxmlLoader.load();
            EditAnimeController eac = fxmlLoader.getController();
            eac.setData(ac,selectedAnime);
            editAnimeStage = new Stage();
            editAnimeStage.getIcons().add(new Image(iconPath));
            editAnimeStage.setTitle(editingAnime);
            editAnimeStage.setScene(new Scene(root));
            editAnimeStage.setAlwaysOnTop(true);
            editAnimeStage.initModality(Modality.APPLICATION_MODAL);
            editAnimeStage.setOnCloseRequest(windowEvent -> editClose());
            editAnimeStage.setResizable(false);
            editAnimeStage.show();

            editAnimeActive = true;
            scrollingText(yellow, msgWarning(animeModification));
        }
    }

    /** 
     * Open CrunchyRoll link
     * 
     * @return void
     */
    public void linkCrunchyRollLink() {
        openLink(crunchyRollLink);
    }

    /** 
     * Open Link Anime
     *
     * @return void
     */
    @FXML
    void linkAnime() {
        openLink(selectedAnime.getLink());
    }

    /**
     * Press Enter
     *
     * @param KeyEvent keyEvent keyboard press
     * @return void
     */
    public void pressEnter(javafx.scene.input.KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) searchPress();
    }

    /**
     * Edit close
     *
     * @return void
     */
    public void editClose(){
        editAnimeActive=false;
        testoScroll.getChildren().clear();
        scrollingText(red,msgDanger(animeNotEdited));
    }

    /**
     * Add Close
     *
     * @return void
     */
    public void addClose(){
        addAnimeActive=false;
        testoScroll.getChildren().clear();
        scrollingText(red,msgDanger(animeNotAdded));
    }

    /**
     * Set Admin Controller
     *
     * @param AdminController ac Admin Constroller
     * @return void 
     */
    public void setAc(AdminController ac) {
        this.ac = ac;
    }

    /**
     * Set Add Anime Active
     *
     * @param boolean addAnimeActive 
     * @return void
     */
    public void setAddAnimeActive(boolean addAnimeActive) {
        this.addAnimeActive = addAnimeActive;
    }

    /**
     * Set Edit Anime Active
     *
     * @param boolean editAnimeActive
     * @return void
     */
    public void setEditAnimeActive(boolean editAnimeActive) {
        this.editAnimeActive = editAnimeActive;
    }

    /**
     * Set Long Messagge
     *
     * @param boolean LongMessagge
     * @return void
     */
    public void setLongMessagge(boolean LongMessagge) {
        this.LongMessagge = LongMessagge;
    }

    /**
     * Scrolling Text
     *
     * @return void
     */
    public void scrollingText(Color color, String text) {
        if(cnt2!=0){ resetScroll(); }
        Text scrollingText = new Text(text);
        testoScroll.getChildren().add(scrollingText);
        testoScroll.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        scrollingText.setEffect(glow);
        scrollingText.setStyle(scrollingTextFX);
        scrollingText.setFill(color);
        scrollingText.setLayoutX(0);
        scrollingText.setLayoutY(0);
        scrollingText.setWrappingWidth(0);

        int time = text.length()/4*1000+1000;
        tt = new TranslateTransition(Duration.millis(time), scrollingText);
        if(LongMessagge){
            tt.setFromX(0 - scrollingText.getWrappingWidth() - 410);
            tt.setToX(scrollingText.getWrappingWidth()+30);
        } else{
            tt.setFromX(0 - scrollingText.getWrappingWidth() - 350);
            tt.setToX(scrollingText.getWrappingWidth()+50);
        }

        LongMessagge=false;
        tt.setCycleCount(1);
        tt.setAutoReverse(false);
        tt.play();
        cnt2++;
        runLater(time);

    }

    /**
     * Reset Scroll description
     *
     * @return void
     */
    private void resetScroll(){
        try{
        testoScroll.getChildren().clear();
        tt.stop();
        timer.purge();
        timer.cancel();
    }catch (Exception ignored){}}

    /**
     * Run Later
     *
     * @param int time (delay)
     * @return void
     */
    public void runLater(int time) {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> testoScroll.getChildren().clear());
            }
        };
        time *= 2;
        timer.schedule(timerTask, time, time);
    }
}
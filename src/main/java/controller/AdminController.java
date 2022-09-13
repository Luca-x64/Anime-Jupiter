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
    private boolean messLungo = false;

    EventHandler<MouseEvent> editHandler = mouseEvent -> { try { editAnime(); } catch (IOException ignored) {} };
    EventHandler<MouseEvent> deleteHandler = mouseEvent -> deleteAnime();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reload(getAnimeList());
    }

    @FXML
    void sort(){
        inputBox.setText("");
        switch (cnt) {
            case 0 -> {
                reload(sortTitle(true));
                sortButton.setText(" A ↓ Z ");
            }
            case 1 -> {
                reload(sortTitle(false));
                sortButton.setText(" Z ↓ A ");
            }
            case 2 -> {
                reload(sortYear(false));
                sortButton.setText("Y+ ↓ Y-");
            }
            case 3 -> {
                reload(sortYear(true));
                sortButton.setText("Y- ↓ Y+");
            }
            default -> {
                reload(getAnimeList());
                sortButton.setText("Ordina");
                cnt = -1;
            }
        }
        cnt++;
    }
    @FXML
    void backToStart() throws IOException {
        ttlJupiter.setOnMouseClicked(null);
        ttlAnime.setOnMouseClicked(null);
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource(guiFolder+"start.fxml"))));
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

    @FXML
    void searchPress() {
        resetScroll();
        String input = stringFormat(inputBox.getText());
        if (input.length() > 0) {
            List<Anime> resultQuery = query(input);
            if (resultQuery.size() > 0) reload(resultQuery);
            else {
                reload(new ArrayList<>());
                scrollingText(red,msgDanger("Nessun anime trovato"));
            }
        } else {
            reload(getAnimeList());
            resetScroll();
        }
    }

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
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(guiFolder+"card.fxml"));
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

    private void setChosenAnime(Anime anime) {
        if (anime == null) {
            this.selectedAnime = null;
            animeTitle.setText("");
            animeData.setText("");
            animeImg.setImage(new Image(imgDefaultRelativePath));
            chosenAnime.setStyle("-fx-background-color: #121619;\n" + "    -fx-background-radius: 30;"); //sposta da qui
            animeDelete.setOnMouseClicked(null);
            animeEdit.setOnMouseClicked(null);
        } else {
            this.selectedAnime = anime;
            animeTitle.setText(anime.getTitle());
            animeData.setText(anime.toString());
            try {
                animeImg.setImage(loadImage(anime.getImagePath()));
            } catch (Exception ignored) {
                animeImg.setImage(new Image(imgDefaultRelativePath));
            }
            chosenAnime.setStyle("-fx-background-color: #121619;\n" + "    -fx-background-radius: 30;");
            animeDelete.setOnMouseClicked(deleteHandler);
            animeEdit.setOnMouseClicked(editHandler);
        }
    }

    @FXML
    void addAnimeclick() throws IOException {
        if(!addAnimeActive){
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(guiFolder+"addAnime.fxml")));
            Parent root = fxmlLoader.load();
            AddAnimeController aac = fxmlLoader.getController();
            aac.setAc(ac);
            addAnimeStage = new Stage();
            addAnimeStage.getIcons().add(new Image(imgProjectFolder+"icon.png"));
            addAnimeStage.setTitle("Aggiungi anime");
            addAnimeStage.setScene(new Scene(root));
            addAnimeStage.setAlwaysOnTop(true);
            addAnimeStage.initModality(Modality.APPLICATION_MODAL);
            addAnimeStage.setOnCloseRequest(windowEvent -> addClose());
            addAnimeStage.setResizable(false);
            addAnimeStage.show();
            addAnimeActive = true;
            scrollingText(yellow, msgWarning("Aggiunta anime"));
        }
    }

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
        scrollingText(red, msgDanger("Anime eliminato"));
    }

    @FXML
    void editAnime() throws IOException {
        if(!editAnimeActive){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Objects.requireNonNull(getClass().getResource(guiFolder+"editAnime.fxml")));
            Parent root = fxmlLoader.load();
            EditAnimeController eac = fxmlLoader.getController();
            eac.setData(ac,selectedAnime);
            editAnimeStage = new Stage();
            editAnimeStage.getIcons().add(new Image(imgProjectFolder+"icon.png"));
            editAnimeStage.setTitle("Modifica anime");
            editAnimeStage.setScene(new Scene(root));
            editAnimeStage.setAlwaysOnTop(true);
            editAnimeStage.initModality(Modality.APPLICATION_MODAL);
            editAnimeStage.setOnCloseRequest(windowEvent -> editClose());
            editAnimeStage.setResizable(false);
            editAnimeStage.show();
            editAnimeActive = true;
            scrollingText(yellow, msgWarning("Anime in modifica"));
        }
    }

    public void linkTelegram() {
        openLink(crunchyrollLink);
    }
    @FXML
    void linkAnime() {
        openLink(selectedAnime.getLink());
    }

    public void pressEnter(javafx.scene.input.KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) searchPress();
    }

    public void editClose(){
        editAnimeActive=false;
        testoScroll.getChildren().clear();
        scrollingText(red,msgDanger("Anime non modificato"));
    }
    public void addClose(){
        addAnimeActive=false;
        testoScroll.getChildren().clear();
        scrollingText(red,msgDanger("Anime non aggiunto"));
    }

    public void setAc(AdminController ac) {
        this.ac = ac;
    }

    public void setAddAnimeActive(boolean addAnimeActive) {
        this.addAnimeActive = addAnimeActive;
    }

    public void setEditAnimeActive(boolean editAnimeActive) {
        this.editAnimeActive = editAnimeActive;
    }
    public void setMessLungo(boolean messLungo) {
        this.messLungo = messLungo;
    }


    public void scrollingText(Color color, String text) {
        if(cnt2!=0){
            resetScroll();
        }
        Text scrollingText = new Text(text);
        testoScroll.getChildren().add(scrollingText);
        testoScroll.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        scrollingText.setEffect(glow);
        scrollingText.setStyle(" -fx-font: bold 18pt \"Tahoma\";");
        scrollingText.setFill(color);
        scrollingText.setLayoutX(0);
        scrollingText.setLayoutY(0);
        scrollingText.setWrappingWidth(0);
        int time = text.length()/4*1000+1000;
        tt = new TranslateTransition(Duration.millis(time), scrollingText);
        if(messLungo){
            tt.setFromX(0 - scrollingText.getWrappingWidth() - 410);
            tt.setToX(scrollingText.getWrappingWidth()+30);
        } else{
            tt.setFromX(0 - scrollingText.getWrappingWidth() - 350);
            tt.setToX(scrollingText.getWrappingWidth()+50);
        }
        messLungo=false;
        tt.setCycleCount(1);
        tt.setAutoReverse(false);
        tt.play();
        cnt2++;
        runLater(time);

    }
    private void resetScroll(){
        try{
        testoScroll.getChildren().clear();
        tt.stop();
        timer.purge();
        timer.cancel();
    }catch (Exception ignored){}
    }


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
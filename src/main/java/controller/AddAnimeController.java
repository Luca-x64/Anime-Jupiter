package controller;

import engine.Engine;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Data;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddAnimeController extends Engine implements Initializable,Data {
    @FXML
    private TextField titleBox,authorBox, editorBox, episodeBox, linkBox, tramaBox, yearBox;
    @FXML
    private ImageView imgview;
    @FXML
    private ProgressBar progessBar;

    private AdminController ac;
    private String imgSelectedPath = null;
    private File selectedFile;
    private boolean ttl = true,aut = true,edi = true,epi = true,link = true,tra = true,year = true;

    @FXML
    void inserisciImmagine() {
        try {
            Stage stage = new Stage();
            stage.setAlwaysOnTop(true);
            ac.addAnimeStage.setAlwaysOnTop(false);
            FileChooser fc = new FileChooser();
            fc.setTitle("Scegli un'immagine");
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image", extPng, extJpg));
            fc.setInitialDirectory(new File(absolutePath));
            selectedFile = fc.showOpenDialog(stage);
            String fileName = selectedFile.getName().trim().replace(" ", "");
            imgSelectedPath = absolutePath + fileName;
            imgview.setImage(new Image(selectedFile.getPath()));
            addProgress();
        } catch (Exception ignored) {
            System.out.println(ignored);
        }
    }


    public void aggiungiAnime(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        int exit = 0;
        String ttl = titleBox.getText().trim(), aut = authorBox.getText().trim(), edi = editorBox.getText().trim(), epi = episodeBox.getText().trim(), y = yearBox.getText().trim(), tr = tramaBox.getText().trim(), link = linkBox.getText().trim();
        if(checkDuplicatesAdd(getAnimeList(), ttl)){
            if (ttl.length() > 0 && aut.length() > 0 && edi.length() > 0 && epi.length() > 0 && y.length() > 0 && tr.length() > 0 && link.length() > 0) {
                if (imgSelectedPath == null) imgSelectedPath = imgDefaultPath;
                // Save img into src/img
                if (!imgSelectedPath.equalsIgnoreCase(imgDefaultPath)){
                    BufferedImage bi = ImageIO.read(selectedFile.toURI().toURL());
                    String fileName = selectedFile.getName().trim().replace(" ", "");
                    String format = fileName.substring(fileName.lastIndexOf(".")).trim();
                    fileName = ttl.toLowerCase(Locale.ROOT).replace(" ","-") + format;
                    imgSelectedPath=absolutePath+fileName;
                    File newFile =  new File(imgSelectedPath);
                    format = format.replace(".","");
                    ImageIO.write(bi, format,newFile);

                }
                try{
                    ac.addAnime(titleBox.getText(), authorBox.getText(), editorBox.getText(), Integer.parseInt(episodeBox.getText()), Integer.parseInt(yearBox.getText()), tramaBox.getText(), imgSelectedPath, linkBox.getText());
                    ac.reload(ac.getAnimeList());
                }catch (NumberFormatException en){  exit = 1; } catch (Exception ignored) {}
            } else {
                exit = 2;
            }
        } else {
            exit = 3;
        }
        switch (exit) {
            case 0 -> {
                Node source = (Node) mouseEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                ac.setAddAnimeActive(false);
                ac.scrollingText(green, msgSuccess("Anime aggiunto"));
            }
            case 1 -> {
                ac.setMessLungo(true);
                ac.scrollingText(red, msgDanger("Anno ed episodi sono numeri"));
                ac.setAddAnimeActive(false);
            }
            case 2 -> {
                ac.setMessLungo(true);
                ac.scrollingText(red, msgDanger("Campi vuoti non ammessi"));
            }
            case 3 -> {
                ac.scrollingText(yellow, msgWarning("Anime già presente"));
                Node source = (Node) mouseEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                ac.reload(ac.getAnimeList());
                ac.setAddAnimeActive(false);
            }
        }
    }
    public void closeEscape(javafx.scene.input.KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.ESCAPE) {
            Node source = (Node) keyEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            ac.addClose();
        }
    }
    public void setAc(AdminController ac) {
        this.ac = ac;
    }

    EventHandler<KeyEvent> titleHandler = event -> titleProgress();
    EventHandler<KeyEvent> authorHandler = event -> authorProgress();
    EventHandler<KeyEvent> studioHandler = event -> studioProgress();
    EventHandler<KeyEvent> episodeHandler = event -> episodeProgress();
    EventHandler<KeyEvent> yearHandler = event -> yearProgress();
    EventHandler<KeyEvent> tramaHandler = event -> tramaProgress();
    EventHandler<KeyEvent> linkHandler = event -> linkProgress();

    private void addProgress(){
        progessBar.setProgress(progessBar.getProgress()+0.125);
    }

    private void removeProgress(){
        progessBar.setProgress(progessBar.getProgress()-0.125);
    }

    private void titleProgress(){
        if(ttl && titleBox.getText().length()>0){
            addProgress();
            ttl=false;
        }else if(!ttl && titleBox.getText().length()== 0){
            removeProgress();
            ttl=true;
        }
    }
    private void authorProgress() {
        if(aut && authorBox.getText().length()>0){
            addProgress();
            aut=false;
        }else if(!aut && authorBox .getText().length()== 0){
            removeProgress();
            aut=true;
        }
    }
    private void studioProgress() {
        if(edi && editorBox.getText().length()>0){
            addProgress();
            edi=false;
        }else if(!edi && editorBox.getText().length()== 0){
            removeProgress();
            edi=true;
        }
    }

    private void episodeProgress(){
        if(epi && episodeBox.getText().length()>0){
            addProgress();
            epi=false;
        }else if(!epi && episodeBox.getText().length()== 0){
            removeProgress();
            epi=true;
        }
    }
    private void yearProgress(){
        if(year && yearBox.getText().length()>0){
            addProgress();
            year=false;
        }else if(!year && yearBox.getText().length()== 0){
            removeProgress();
            year=true;
        }
    }
    private void tramaProgress() {
        if(tra && tramaBox.getText().length()>0){
            addProgress();
            tra=false;
        }else if(!tra && tramaBox.getText().length()== 0){
            removeProgress();
            tra=true;
        }
    }
    private void linkProgress() {
        if(link && linkBox.getText().length()>0){
            addProgress();
            link=false;
        }else if(!link && linkBox.getText().length()== 0){
            removeProgress();
            link=true;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleBox.setOnKeyReleased(titleHandler);
        authorBox.setOnKeyReleased(authorHandler);
        editorBox.setOnKeyReleased(studioHandler);
        episodeBox.setOnKeyReleased(episodeHandler);
        yearBox.setOnKeyReleased(yearHandler);
        tramaBox.setOnKeyReleased(tramaHandler);
        linkBox.setOnKeyReleased(linkHandler);
    }
}
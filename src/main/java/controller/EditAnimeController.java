package controller;

import engine.Engine;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Anime;
import javax.imageio.ImageIO;

import app.Data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditAnimeController extends Engine {

    @FXML
    private TextField authorBox, publisherBox, episodeBox, linkBox, titleBox, plotBox, yearBox;
    @FXML
    private ImageView imgview;
    private AdminController ac;
    private String imgSelectedPath = null;
    private File selectedFile;
    private Anime animeSelected;

    /**
     * Insert Image
     * 
     * @return void
     */
    @FXML
    void insertImage() {
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        ac.editAnimeStage.setAlwaysOnTop(false);

        FileChooser fc = new FileChooser();
        fc.setTitle(Data.fcTitle);
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(Data.description, Data.extPng,Data.extJpg));
        fc.setInitialDirectory(new File(Data.imgAbsFolder));
        selectedFile = fc.showOpenDialog(stage);

        try {
            String fileName = selectedFile.getName().trim().replace(Data.space, Data.empty);
            imgSelectedPath = Data.imgAbsFolder + fileName;

            imgview.setImage(new Image(selectedFile.getPath()));
        } catch (Exception ignored) {}
    }

    /**
     * Confirm Image 
     *
     * @param MouseEvent mouseEvent click
     * @exception IOException
     * @return void
     */
    public void confirm(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        boolean result = false;
        String ttl = titleBox.getText().trim(), aut = authorBox.getText().trim(), edi = publisherBox.getText().trim(), epi = episodeBox.getText().trim(), y = yearBox.getText().trim(), tr = plotBox.getText().trim(), link = linkBox.getText().trim();
        
        if (ttl.length() > 0 && aut.length() > 0 && edi.length() > 0 && epi.length() > 0 && y.length() > 0 && tr.length() > 0 && link.length() > 0) {
            if(stringFormat(ttl).equals(stringFormat(animeSelected.getTitle())) && stringFormat(aut).equals(stringFormat(animeSelected.getAuthor())) && stringFormat(edi).equals(stringFormat(animeSelected.getPublisher())) && stringFormat(epi).equals(stringFormat(String.valueOf(animeSelected.getEpisodes()))) && stringFormat(y).equals(stringFormat(String.valueOf(animeSelected.getYear()))) && stringFormat(tr).equals(stringFormat(animeSelected.getPlot())) && stringFormat(link).equals(stringFormat(animeSelected.getLink())) && stringFormat(imgSelectedPath).equals(stringFormat(animeSelected.getImagePath()))){
            } else {
                List<Anime> alCopy = new ArrayList<>(ac.getAnimeList());
                int pos = alCopy.indexOf(animeSelected);
                alCopy.remove(animeSelected);

                if(alCopy.stream().noneMatch(e->stringFormat(e.getTitle()).equals(stringFormat(ttl)))){
                    if(!imgSelectedPath.equalsIgnoreCase(animeSelected.getImagePath())){
                        BufferedImage bi = ImageIO.read(selectedFile.toURI().toURL());
                        String fileName = selectedFile.getName().trim().replace(Data.space, Data.empty);
                        String format = fileName.substring(fileName.lastIndexOf(Data.dot)).trim();
                        fileName = ttl.toLowerCase(Locale.ROOT).replace(Data.space,Data.dash) + format;
                        imgSelectedPath=Data.imgAbsFolder+fileName;
                        File newFile =  new File(imgSelectedPath);
                        format = format.replace(Data.dot,Data.empty);
                        ImageIO.write(bi, format,newFile);
                    }
                    Anime newAnime;
                    try{
                        newAnime = new Anime(ttl, aut, edi, Integer.valueOf(epi), Integer.valueOf(y), tr, imgSelectedPath, link);
                        alCopy.add(pos, newAnime);
                        ac.setAnimeList(alCopy);
                        ac.reloadFile();
                        result=true;
                    }catch (NumberFormatException ne){
                        ac.setLongMessagge(true);
                        ac.scrollingText(Data.red,msgDanger(Data.yearEpisodeNumber));
                    }catch (Exception ignored){}
                }else{
                    ac.scrollingText(Data.yellow,msgWarning (Data.animeAlreadyPresent));
                }
            }

        }else{
            ac.scrollingText(Data.red,msgDanger(Data.blankField));
        }

        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        ac.reload(ac.getAnimeList());
        ac.setEditAnimeActive(false);
        if(result) { ac.scrollingText(Data.green,msgSuccess(Data.animeEdited)); }
        else { ac.editClose(); }
    }

    /**
     * Set Data
     *
     * @param AdminController ac admin controller
     * @param Anime a anime
     * @return void
     */
    public void setData(AdminController ac, Anime a) {
        this.ac = ac;
        this.animeSelected = a;

        titleBox.setText(a.getTitle());
        authorBox.setText(a.getAuthor());
        publisherBox.setText(a.getPublisher());
        episodeBox.setText(String.valueOf(a.getEpisodes()));
        yearBox.setText(String.valueOf(a.getYear()));
        plotBox.setText(a.getPlot());
        linkBox.setText(a.getLink());

        try {
            File file = new File(a.getImagePath());
            FileInputStream fIStream = new FileInputStream(file);
            Image image = new Image(fIStream);
            imgview.setImage(image);
            imgSelectedPath = a.getImagePath();
        } catch (Exception ignored) {
            imgSelectedPath = Data.imgAbsFolder;
        }
    }

    /**
     * Close Escape
     *
     * @param KeyEvent keyEvent keyboard press
     * @return void
     */
    public void closeEscape(javafx.scene.input.KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.ESCAPE) {
            Node source = (Node) keyEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            ac.editClose();
        }
    }
}
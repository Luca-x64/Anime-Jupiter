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

import javax.imageio.ImageIO;

import app.Data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddAnimeController extends Engine implements Initializable {
    @FXML
    private TextField titleBox, authorBox, publisherBox, episodeBox, linkBox, plotBox, yearBox;
    @FXML
    private ImageView imgview;
    @FXML
    private ProgressBar progessBar;

    private AdminController ac;
    private String imgSelectedPath = null;
    private File selectedFile;
    private boolean ttl = true, aut = true, pub = true, epi = true, link = true, plo = true, year = true;

    /**
     * Insert Image
     * 
     * @return void
     */
    @FXML
    void insertImage() {
        try {
            Stage stage = new Stage();
            stage.setAlwaysOnTop(true);
            ac.addAnimeStage.setAlwaysOnTop(false);

            FileChooser fc = new FileChooser();
            fc.setTitle(Data.fcTitle);
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(Data.description, Data.extPng, Data.extJpg));
            fc.setInitialDirectory(new File(Data.imgAbsFolder));
            selectedFile = fc.showOpenDialog(stage);

            String fileName = selectedFile.getName().trim().replace(Data.space,Data.empty);
            imgSelectedPath = Data.imgAbsFolder + fileName;
            imgview.setImage(new Image(selectedFile.getPath()));
            addProgress();
        } catch (Exception ignored) {
            System.out.println(ignored);
        }
    }

    /**
     * Add Anime
     * 
     * @param MouseEvent mouseEvent click
     * @throws IOException
     * @return void
     */
    public void addAnime(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        int exit = 0;
        String ttl = titleBox.getText().trim(), aut = authorBox.getText().trim(), pub = publisherBox.getText().trim(),
                epi = episodeBox.getText().trim(), y = yearBox.getText().trim(), tr = plotBox.getText().trim(),
                link = linkBox.getText().trim();
        if (checkDuplicatesAdd(getAnimeList(), ttl)) {
            if (ttl.length() > 0 && aut.length() > 0 && pub.length() > 0 && epi.length() > 0 && y.length() > 0
                    && tr.length() > 0 && link.length() > 0) {
                if (imgSelectedPath == null) {
                    imgSelectedPath = Data.imgDefaultRelPath;
                }
                // Save img into src/img
                if (!imgSelectedPath.equalsIgnoreCase(Data.imgDefaultRelPath)) {
                    BufferedImage bi = ImageIO.read(selectedFile.toURI().toURL());
                    String fileName = selectedFile.getName().trim().replace(Data.space,Data.empty);
                    String format = fileName.substring(fileName.lastIndexOf(Data.dot)).trim();
                    fileName = ttl.toLowerCase(Locale.ROOT).replace(Data.space, Data.dash) + format;
                    imgSelectedPath = Data.imgAbsFolder + fileName;
                    File newFile = new File(imgSelectedPath);
                    format = format.replace(Data.dot, Data.empty);
                    ImageIO.write(bi, format, newFile);
                }
                try {
                    ac.addAnime(titleBox.getText(), authorBox.getText(), publisherBox.getText(),
                            Integer.parseInt(episodeBox.getText()), Integer.parseInt(yearBox.getText()),
                            plotBox.getText(), imgSelectedPath, linkBox.getText());
                    ac.reload(ac.getAnimeList());
                } catch (NumberFormatException en) {
                    exit = 1;
                } catch (Exception ignored) {
                }
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
                ac.scrollingText(Data.green, msgSuccess(Data.animeAdded));
            }
            case 1 -> {
                ac.setLongMessagge(true);
                ac.scrollingText(Data.red, msgDanger(Data.yearEpisodesAreNumbers));
                ac.setAddAnimeActive(false);
            }
            case 2 -> {
                ac.setLongMessagge(true);
                ac.scrollingText(Data.red, msgDanger(Data.blankField));
            }
            case 3 -> {
                ac.scrollingText(Data.yellow, msgWarning(Data.animeAlreadyPresent));

                Node source = (Node) mouseEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                ac.reload(ac.getAnimeList());
                ac.setAddAnimeActive(false);
            }
        }
    }

    /**
     * Close Escape
     * 
     * @param KeyEvent keyEvent keyboard press
     * @return void
     */
    public void closeEscape(javafx.scene.input.KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            Node source = (Node) keyEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            ac.addClose();
        }
    }

    /**
     * Set Admin Controller
     * 
     * @param AdminController ac admin controller
     * @return void
     */
    public void setAc(AdminController ac) {
        this.ac = ac;
    }

    // Event Handlers
    EventHandler<KeyEvent> titleHandler = event -> titleProgress();
    EventHandler<KeyEvent> authorHandler = event -> authorProgress();
    EventHandler<KeyEvent> publisherHandler = event -> publisherProgress();
    EventHandler<KeyEvent> episodeHandler = event -> episodeProgress();
    EventHandler<KeyEvent> yearHandler = event -> yearProgress();
    EventHandler<KeyEvent> plotHandler = event -> plotProgress();
    EventHandler<KeyEvent> linkHandler = event -> linkProgress();

    /**
     * Add Progress
     *
     * @return void
     */
    private void addProgress() {
        progessBar.setProgress(progessBar.getProgress() + 0.125);
    }

    /**
     * Remove Progress
     *
     * @return void
     */
    private void removeProgress() {
        progessBar.setProgress(progessBar.getProgress() - 0.125);
    }

    /**
     * Title Progress
     *
     * @return void
     */
    private void titleProgress() {
        if (ttl && titleBox.getText().length() > 0) {
            addProgress();
            ttl = false;
        } else if (!ttl && titleBox.getText().length() == 0) {
            removeProgress();
            ttl = true;
        }
    }

    /**
     * Author Progress
     *
     * @return void
     */
    private void authorProgress() {
        if (aut && authorBox.getText().length() > 0) {
            addProgress();
            aut = false;
        } else if (!aut && authorBox.getText().length() == 0) {
            removeProgress();
            aut = true;
        }
    }

    /**
     * Publisher Progress
     *
     * @return void
     */
    private void publisherProgress() {
        if (pub && publisherBox.getText().length() > 0) {
            addProgress();
            pub = false;
        } else if (!pub && publisherBox.getText().length() == 0) {
            removeProgress();
            pub = true;
        }
    }

    /**
     * Episode Progress
     *
     * @return void
     */
    private void episodeProgress() {
        if (epi && episodeBox.getText().length() > 0) {
            addProgress();
            epi = false;
        } else if (!epi && episodeBox.getText().length() == 0) {
            removeProgress();
            epi = true;
        }
    }

    /**
     * Year Progress
     *
     * @return void
     */
    private void yearProgress() {
        if (year && yearBox.getText().length() > 0) {
            addProgress();
            year = false;
        } else if (!year && yearBox.getText().length() == 0) {
            removeProgress();
            year = true;
        }
    }

    /**
     * Plot Progress
     *
     * @return void
     */
    private void plotProgress() {
        if (plo && plotBox.getText().length() > 0) {
            addProgress();
            plo = false;
        } else if (!plo && plotBox.getText().length() == 0) {
            removeProgress();
            plo = true;
        }
    }

    /**
     * Link Progress
     *
     * @return void
     */
    private void linkProgress() {
        if (link && linkBox.getText().length() > 0) {
            addProgress();
            link = false;
        } else if (!link && linkBox.getText().length() == 0) {
            removeProgress();
            link = true;
        }
    }

    /**
     * Initialize
     * 
     * @param URL url
     * @param ResourceBundle resourceBundle
     * @return void
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleBox.setOnKeyReleased(titleHandler);
        authorBox.setOnKeyReleased(authorHandler);
        publisherBox.setOnKeyReleased(publisherHandler);
        episodeBox.setOnKeyReleased(episodeHandler);
        yearBox.setOnKeyReleased(yearHandler);
        plotBox.setOnKeyReleased(plotHandler);
        linkBox.setOnKeyReleased(linkHandler);
    }
}
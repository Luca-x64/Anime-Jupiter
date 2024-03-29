package controller;

import engine.Engine;
import interfaces.SetDataEdit;
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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditAnimeController extends Engine implements SetDataEdit {

    @FXML
    private TextField authorBox, publisherBox, episodeBox, linkBox, titleBox, plotBox, yearBox;
    @FXML
    private ImageView imgview;
    private String imgPath = null;
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

        FileChooser fc = new FileChooser();
        fc.setTitle(fcTitle);
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(description, extPng, extJpg));
        fc.setInitialDirectory(new File(imgAbsFolder));
        selectedFile = fc.showOpenDialog(stage);

        try {
            String fileName = selectedFile.getName().trim().replace(space, empty);
            imgPath = imgAbsFolder + fileName;

            imgview.setImage(new Image(selectedFile.getPath()));
        } catch (Exception ignored) {
        }
    }

    /**
     * Confirm Image
     *
     * @param MouseEvent mouseEvent click
     * @exception IOException
     * @return void
     */
    public void confirm(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        int exit = 0;
        String ttl = titleBox.getText().trim(), aut = authorBox.getText().trim(), edi = publisherBox.getText().trim(),
                epi = episodeBox.getText().trim(), y = yearBox.getText().trim(), tr = plotBox.getText().trim(),
                link = linkBox.getText().trim();

        if (ttl.length() > 0 && aut.length() > 0 && edi.length() > 0 && epi.length() > 0 && y.length() > 0
                && tr.length() > 0 && link.length() > 0) {
            if (stringFormat(ttl).equals(stringFormat(animeSelected.getTitle()))
                    && stringFormat(aut).equals(stringFormat(animeSelected.getAuthor()))
                    && stringFormat(edi).equals(stringFormat(animeSelected.getPublisher()))
                    && stringFormat(epi).equals(stringFormat(String.valueOf(animeSelected.getEpisodes())))
                    && stringFormat(y).equals(stringFormat(String.valueOf(animeSelected.getYear())))
                    && stringFormat(tr).equals(stringFormat(animeSelected.getPlot()))
                    && stringFormat(link).equals(stringFormat(animeSelected.getLink()))
                    && stringFormat(imgPath).equals(stringFormat(animeSelected.getImagePath()))) {
                exit = 1;
            } else {

                receiveAllAnime();

                List<Anime> alCopy = new ArrayList<>(getAnimeList());
                if (alCopy != null) {
                    alCopy.removeIf(e -> e.getID() == animeSelected.getID());

                    if (alCopy.stream()
                            .noneMatch(e -> stringFormat(e.getTitle()).equalsIgnoreCase(stringFormat(ttl)))) {
                        if (!imgPath.equalsIgnoreCase(animeSelected.getImagePath())) {
                            BufferedImage bi = ImageIO.read(selectedFile.toURI().toURL());
                            String fileName = selectedFile.getName().trim().replace(space, empty);
                            String format = fileName.substring(fileName.lastIndexOf(dot)).trim();
                            fileName = ttl.toLowerCase(Locale.ROOT).replace(space, dash) + format;
                            imgPath = imgAbsFolder + fileName;
                            File newFile = new File(imgPath);
                            format = format.replace(dot, empty);
                            ImageIO.write(bi, format, newFile);
                        }

                        try {
                            editAnime(animeSelected.getID(), ttl, aut, edi, Integer.valueOf(epi), Integer.valueOf(y),
                                    tr,
                                    imgPath, link);

                        } catch (NumberFormatException ne) {
                            exit = 2;
                        } catch (Exception ignored) {
                            // System.err.println(ignored);
                            exit = 3;
                        }
                    } else {
                        exit = 4;
                    }
                }
            }

        } else {
            exit = 5;
        }
        switch (exit) {
            case 0: {
                Node source = (Node) mouseEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                setExitMessagge(false, green, msgSuccess(animeEdited));
                break;
            }
            case 1: {
                setExitMessagge(false, yellow, msgWarning("Fields are equal"));
                break;
            }
            case 2: {
                setExitMessagge(true, red, msgDanger(yearEpisodeNumber));
                break;
            }
            case 3: {
                setExitMessagge(false, red, msgDanger(animeNotEdited));
                break;
            }
            case 4: {
                setExitMessagge(false, yellow, msgWarning(animeAlreadyPresent));
                break;
            }
            case 5: {
                setExitMessagge(false, red, msgDanger(blankField));
                break;
            }
            default: { // CHECK if needed
                System.out.println("case exit default edit");
            }
        }

        // CHECK forse
        // setEditAnimeActive(false);
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
        }
    }

    /**
     * Set Data
     *
     * @param AdminController ac admin controller
     * @param Anime           a anime
     * @return void
     */
    @Override
    public void setData(Anime a, ObjectOutputStream os, ObjectInputStream is) {
        super.setStream(os, is);
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
            imgPath = a.getImagePath();
        } catch (Exception ignored) {
            imgPath = imgAbsFolder;
        }
    }

}
package main;

import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;

public interface Data {

    // Declaration of all the costants

    // Title

    String projectName = "Anime Jupiter";

    // Path costants
    String resources = "main/resources/";
    String img = "img/";
    String data = "data/";
    String project = "project/";
    String gui = "gui/";
    String src = "src/";
    String preview = "preview.png";
    String filename = "animeData.txt";

    String icon = "icon.png";

    String imgDataFolder = img+data;
    String imgProjectFolder = img+project;
    String imgAbsFolder = src + resources + imgDataFolder;

    String iconPath = imgProjectFolder+icon;

    String imgDefaultRelPath = imgDataFolder+preview;
    String imgDefaultAbsPath = imgAbsFolder + preview;
    String absFilePath = src + resources +data+filename;


    String guiFolder = "/"+gui;
    String guiStart = guiFolder + "start.fxml";
    String guiUser = guiFolder+"user.fxml";
    String guiAdmin = guiFolder+"admin.fxml";
    String guiCard = guiFolder+"card.fxml";
    String guiAddAnime = guiFolder+"addAnime.fxml";

    // Regex separator
    String regex = " §_£% ";
    String crunchyRollLink = "https://www.crunchyroll.com/it/videos/anime";

    // File Chooser
    String fcTitle = "Choose an Image";

    // Extension Filter
    String description = "Image";

    // Extensions
    String extPng = "*.png";
    String extJpg = "*.jpg";

    // Color and Effect
    Color red = Color.RED;
    Color yellow = Color.YELLOW;
    Color green = Color.GREEN;
    Effect glow = new Glow(0.5);


    //todo revisionare da qui in su

    // Sort Button text

    String sort = " Sort ";
    String orderAlpha = " A ↓ Z ";
    String reversedAlpha = " Z ↓ A ";
    String orderYear = "Y+ ↓ Y-";
    String reversedYear = "Y- ↓ Y+";

    // Scrolling messagges

    String noAnime = "No anime found";
    String yearEpisodeNumber = "Years and Episodes have to be a number";
    String animeAlreadyPresent = "Anime Already Present";
    String blankField = "Blank fields not allowed";
    String animeEdited = "Anime edited";

    // Empty Strings

    String empty = "";
    String space = " ";

    // Symbols
    String dot = ".";
    String dash ="-";

    // Style

    String chosenAnimeFX = "-fx-background-color: #121619;\n" + "    -fx-background-radius: 30;";
    String scrollingTextFX = " -fx-font: bold 18pt \"Tahoma\";";


}
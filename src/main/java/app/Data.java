package app;

import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;

public class Data {

    // Declaration of all the costants

    // Title
    public static String projectName = "Anime Jupiter";
 
    // Path  
    public static String  resources = "main/resources/";
    public static String  img = "img/";
    public static String  data = "data/";
    public static String  project = "project/";
    public static String  gui = "gui/";
    public static String  src = "src/";
    public static String  preview = "preview.png";
    public static String  filename = "animeData.txt";
 
    public static String  icon = "icon.png";
 
    public static String  imgDataFolder = img+data;
    public static String  imgProjectFolder = img+project;
    public static String  imgAbsFolder = src + resources + imgDataFolder;
 
    public static String  iconPath = imgProjectFolder+icon;
 
    public static String  imgDefaultRelPath = imgDataFolder+preview;
    public static String  imgDefaultAbsPath = imgAbsFolder + preview;
    public static String  absFilePath = src + resources +data+filename;
 
    public static String  guiFolder = "/"+gui;
    public static String  guiStart = guiFolder + "start.fxml";
    public static String  guiUser = guiFolder+"user.fxml";
    public static String  guiAdmin = guiFolder+"admin.fxml";
    public static String  guiCard = guiFolder+"card.fxml";
    public static String  guiAddAnime = guiFolder+"addAnime.fxml";
    public static String  guiEditAnime = guiFolder+"editAnime.fxml";
 
    // Rege x separator
    public static String  regex = " §_£% ";
    public static String  crunchyRollLink = "https://www.crunchyroll.com/it/videos/anime";
 
    // File  Chooser
    public static String  fcTitle = "Choose an Image";
 
    // Exte nsion Filter
    public static String  description = "Image";
 
    // Exte nsions
    public static String  extPng = "*.png";
    public static String  extJpg = "*.jpg";
 
    // Colo r and Effect
    public static Color red = Color.RED;
    public static Color yellow = Color.YELLOW;
    public static Color green = Color.GREEN;
    public static Effect  glow = new Glow(0.5);
 
    // Sort  Button text
 
    public static String  sort = " Sort ";
    public static String  orderAlpha = " A ↓ Z ";
    public static String  reversedAlpha = " Z ↓ A ";
    public static String  orderYear = "Y+ ↓ Y-";
    public static String  reversedYear = "Y- ↓ Y+";
 
    // Scro lling messagges
 
    public static String  noAnime = "No Anime found";
    public static String  animeAlreadyPresent = "Anime already present";
    public static String  blankField = "Blank fields not allowed";
 
    public static String  yearEpisodesAreNumbers = "Years and Episodes are numbers";
    public static String  yearEpisodeNumber = "Years and Episodes have to be a number";
 
    public static String  animeEdited = "Anime edited";
    public static String  animeNotEdited = "Anime not edited";
 
    public static String  addAnime = "Add Anime";
    public static String  addingAnime = "Addition anime";
    public static String  animeAdded = "Anime added";
    public static String  animeNotAdded ="Anime not added";
 
    public static String  animeDeleted = "Anime deleted";
 
    public static String  editingAnime = "Edit Anime";
    public static String  animeModification = "Anime in modification";
 
    // Scro lling result
    public static String  success = "✔ ";
    public static String  warning = "⚠ ";
    public static String  danger = "✘ ";
 
    // Empt y Strings
 
    public static String  empty = "";
    public static String  space = " ";
 
    // Symb ols
    public static String  dot = ".";
    public static String  doubleDot= ":";
    public static String  dash ="-";
 
    // New  Line
    public static String  nl = "\n";
 
    // Erro r in open link
    public static String  errorLink = "Error in opening website: ";
 
    // File 
    public static String  fileCreatingError ="Error in creating file: ";
 
    // Anim e Data
    public static String  title = "Title"+doubleDot+space;
    public static String  author = "Author"+doubleDot+space;
    public static String  publisher = "Publisher"+doubleDot+space;
    public static String  episodes = "Episodes"+doubleDot+space;
    public static String  year = "Year"+doubleDot+space;
    public static String  plot = "Plot"+doubleDot+space;
 
    // Styl e
    public static String  chosenAnimeFX = "-fx-background-color: #121619;\n" + "    -fx-background-radius: 30;";
    public static String  scrollingTextFX = " -fx-font: bold 18pt \"Tahoma\";";
}
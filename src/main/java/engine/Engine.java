package engine;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import main.App;
import main.Data;
import model.Anime;
import model.User;

import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.util.*;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;

import com.mysql.cj.xdevapi.PreparableStatement;

import interfaces.StreamController;

public class Engine implements StreamController, Data {
    public List<Anime> animeList = new ArrayList<>();

    private ObjectOutputStream os;
    private ObjectInputStream is;

    private List<Anime> displayedAnimeList;

    /**
     * Engine Constructor
     */
    public Engine() {

    }

    protected void receiveAllAnime() {
        send(1);
        animeList = (ArrayList<Anime>) receive(); // TODO CHECK correttezza runtime
        displayedAnimeList = animeList;
    }

    /**
     * Load Anime
     *
     * @return void
     */
    public void load() {
        animeList.clear();
        animeList.addAll(getAnimeList());
    }

    /**
     * Add Anime
     *
     * @param ttl title
     * @param aut author
     * @param pub pubtor
     * @param epi episodes
     * @param y year
     * @param pl plot
     * @param imgPath image path
     * @param link link
     * @throws IOException
     */
    protected void addAnime(String ttl, String aut, String pub, Integer epi, Integer y, String pl, String imgPath,
            String link) throws IOException {
        Anime anime = new Anime(ttl, aut, pub, epi, y, pl, imgPath, link);
        send(3);
        send(anime);
        boolean response = (Boolean) receive();
        if (response) {
            // System.out.println("Anime added!"); 
            setExitMessagge(false, green, msgSuccess(animeAdded));
            receiveAllAnime();
        } else {
            // System.out.println("Can't add Anime!"); 
            setExitMessagge(false, red, msgWarning(animeNotAdded));
        }
    }

    protected void editAnime(int id, String ttl, String aut, String pub, int epi, int y, String pl, String imgPath,
            String link) {
        Anime editedAnime = new Anime(ttl, aut, pub, epi, y, pl, imgPath, link);
        editedAnime.setID(id);
        send(6);
        send(editedAnime);
        boolean response = (Boolean) receive();
        if (response) {
            //System.out.println("Anime Edited!"); 
            setExitMessagge(false, green, msgSuccess(animeEdited));
            receiveAllAnime();
        } else {
            //System.out.println("Can't edit anime");
            setExitMessagge(false, red, msgWarning(animeNotEdited));
        }
    }

    protected void editOpened() {

    }

    protected void setLowerStream(StreamController sc) {
        sc.setStream(os, is);
    }

    protected void deleteAnime(int id) {
        send(5);
        send(id);
        Boolean response = (Boolean) receive();
        if (response) {
            //System.out.println("Anime deleted!");
            receiveAllAnime();
        } else {
            //System.out.println("Can't delete Anime!");
        }
    }

    /**
     * Query
     * 
     * @param input input field
     * @return List<Anime> query result
     */
    protected List<Anime> query(String input) {
        input = stringFormat(input);
        send(4);
        send(input);
        displayedAnimeList = (ArrayList<Anime>) receive();
        return displayedAnimeList;
    }

    protected void setExitMessagge(boolean longMessagge, Color color, String msg) {
        List<Object> output = new ArrayList<>();
        output.add(longMessagge);
        output.add(color.getRed());
        output.add(color.getGreen());
        output.add(color.getBlue());
        output.add(msg);
        send(8);
        send(output);
    }

    protected List<Object> getExitMessagge() {
        send(9);
        List<Object> output = (ArrayList<Object>) receive();
        return output;
    }

    /**
     * Sort Title
     * 
     * @param b
     * @return List<Anime>
     */
    public List<Anime> sortTitle(boolean b) {
        List<Anime> al = new ArrayList<>(List.copyOf(displayedAnimeList)); // TODO change animeList, to displayed anime
                                                                           // list
        if (b)
            al.sort(Comparator.comparing(Anime::getTitle));
        else
            al.sort(Comparator.comparing(Anime::getTitle).reversed());
        return al;
    }

    /**
     * Sort Year
     * 
     * @param b
     * @return List<Anime>
     */
    public List<Anime> sortYear(boolean b) {
        List<Anime> al = new ArrayList<>(List.copyOf(displayedAnimeList));
        if (b)
            al.sort(Comparator.comparing(Anime::getYear));
        else
            al.sort(Comparator.comparing(Anime::getYear).reversed());
        return al;
    }

    /**
     * String Format
     * 
     * @param s string
     * @return String
     */
    public String stringFormat(String s) {
        return s.toLowerCase(Locale.ROOT).trim();
    }

    /**
     * Check Duplicates Add
     * 
     * @param al anime list
     * @param ttl title
     * @return boolean
     */
    public boolean checkDuplicatesAdd(List<Anime> al, String ttl) {
        return al.stream().noneMatch(e -> stringFormat(e.getTitle()).equals(stringFormat(ttl)));
    }

    protected List<Anime> getSuperFavourite() {
        send(2);
        List<Anime> animeList = (ArrayList<Anime>) receive();
        return animeList;
    }

    protected void logout() {
        send(7);
    }

    /**
     * Messagge Success
     * 
     * @param msg messagge text
     * @return String
     */
    public String msgSuccess(String msg) {
        return success + msg + success;
    }

    /**
     * Messagge Warning
     * 
     * @param msg messagge text
     * @return String
     */
    public String msgWarning(String msg) {
        return warning + msg + warning;
    }

    /**
     * Messagge Danger
     * 
     * @param msg messagge text
     * @return String
     */
    public String msgDanger(String msg) {
        return danger + msg + danger;
    }

    /**
     * Open Link
     *
     * @param url
     */
    public void openLink(String url) {
        Platform.runLater(() -> {
            // Ottieni l'oggetto HostServices dalla piattaforma JavaFX
            HostServices hostServices = App.getHostServicesInstance();

            // Apri l'URL specificato nel browser predefinito
            hostServices.showDocument(url);

        });
        // try {
        // // Specifica l'URL che desideri aprire
        // System.out.println(1);
        // // Verifica se Desktop è supportato sulla piattaforma corrente
        // if (Desktop.isDesktopSupported()) {
        // Desktop desktop = Desktop.getDesktop();
        // System.out.println(2);
        // // Verifica se l'azione di apertura del link è supportata
        // if (desktop.isSupported(Desktop.Action.BROWSE)) {
        // // Apre l'URL specificato nel browser predefinito
        // System.out.println(3);
        // desktop.browse(new URI(url));
        // } else {
        // System.out.println("Desktop browsing is not supported on this platform.");
        // }
        // } else {
        // System.out.println("Desktop non è supportato sulla piattaforma corrente.");
        // }
        // } catch (Exception e) {
        // System.out.println(errorLink + url);
        // }

    }

    /**
     * Load Image
     * 
     * @param path
     * @throws FileNotFoundException
     * @return Image
     */
    public Image loadImage(String path) throws FileNotFoundException {
        File file = new File(path);
        FileInputStream fIStream = new FileInputStream(file);
        return new Image(fIStream);
    }

    protected boolean updateFavourite(int anime_id) {
        boolean success = false;
        send(10);
        send(anime_id);
        success = (Boolean) receive();
        return success;
    }

    private synchronized void send(Object o) {
        try {
            os.writeObject(o);
            os.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private synchronized Object receive() {
        Object received = null;
        try {
            received = is.readObject();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return received;
    }

    // Getter & Setter

    /**
     * Get Anime List
     * 
     * @return List<Anime>
     */
    public List<Anime> getAnimeList() {
        displayedAnimeList = animeList;
        return animeList;
    }

    /**
     * Set Anime List
     * 
     * @param animeList anime list
     */
    public void setAnimeList(List<Anime> animeList) {
        this.animeList = animeList;
    }

    @Override
    public void setStream(ObjectOutputStream os, ObjectInputStream is) {
        this.os = os;
        this.is = is;
    }

}
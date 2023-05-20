package engine;

import javafx.scene.image.Image;
import main.Data;
import model.Anime;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import interfaces.StreamController;

// CHECK why abstact
public class Engine implements StreamController, Data {
    public List<Anime> animeList = new ArrayList<>();
    private Socket socket;
    private ObjectOutputStream os;
    private ObjectInputStream is;

    /**
     * Engine Constructor
     */
    public Engine() {

    }

    protected void receiveAllAnime() {
        send(1);
        animeList = (ArrayList<Anime>) receive(); // TODO CHECK correttezza runtime
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
     * @param String  ttl title
     * @param String  aut author
     * @param String  pub pubtor
     * @param Integer epi episodes
     * @param Integer y year
     * @param String  pl plot
     * @param String  imgPath image path
     * @param String  link link
     * @throws IOException
     * 
     * @return void
     */
    public void addAnime(String ttl, String aut, String pub, Integer epi, Integer y, String pl, String imgPath,
            String link) throws IOException {
        Anime anime = new Anime(ttl, aut, pub, epi, y, pl, imgPath, link);
        send(3);
        send(anime);
        int response = (Integer) receive();
        if (response == 1) {
            System.out.println("Anime added!");
            receiveAllAnime();
        } else {
            System.out.println("Can't add Anime!");
        }
    }

    /**
     * Query
     * 
     * @param String input input field
     * @return List<Anime> query result
     */
    protected List<Anime> query(String input) {
        input = stringFormat(input);
        send(4);
        send(input);
        List<Anime> result = (ArrayList<Anime>) receive();
        System.out.println("len:" +result.size());
        return result;
    }

    /**
     * Sort Title
     * 
     * @param boolean b
     * @return List<Anime>
     */
    public List<Anime> sortTitle(boolean b) {
        List<Anime> al = new ArrayList<>(List.copyOf(animeList));
        if (b)
            al.sort(Comparator.comparing(Anime::getTitle));
        else
            al.sort(Comparator.comparing(Anime::getTitle).reversed());
        return al;
    }

    /**
     * Sort Year
     * 
     * @param boolean b
     * @return List<Anime>
     */
    public List<Anime> sortYear(boolean b) {
        List<Anime> al = new ArrayList<>(List.copyOf(animeList));
        if (b)
            al.sort(Comparator.comparing(Anime::getYear));
        else
            al.sort(Comparator.comparing(Anime::getYear).reversed());
        return al;
    }

    /**
     * String Format
     * 
     * @param String s string
     * @return String
     */
    public String stringFormat(String s) {
        return s.toLowerCase(Locale.ROOT).trim();
    }

    /**
     * Check Duplicates Add
     * 
     * @param List<Anime> al anime list
     * @param String      ttl title
     * @return boolean
     */
    public boolean checkDuplicatesAdd(List<Anime> al, String ttl) {
        return al.stream().noneMatch(e -> stringFormat(e.getTitle()).equals(stringFormat(ttl)));
    }

    /**
     * Messagge Success
     * 
     * @param String msg messagge text
     * @return String
     */
    public String msgSuccess(String msg) {
        return success + msg + success;
    }

    /**
     * Messagge Warning
     * 
     * @param String msg messagge text
     * @return String
     */
    public String msgWarning(String msg) {
        return warning + msg + warning;
    }

    /**
     * Messagge Danger
     * 
     * @param String msg messagge text
     * @return String
     */
    public String msgDanger(String msg) {
        return danger + msg + danger;
    }

    /**
     * Open Link
     *
     * @param String url
     * @return void
     */
    public void openLink(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (Exception e) {
            System.out.println(errorLink + url);
        }
    }

    // Getter & Setter

    /**
     * Get Anime List
     * 
     * @return List<Anime>
     */
    public List<Anime> getAnimeList() {
        return animeList;
    }

    /**
     * Set Anime List
     * 
     * @param List<Anime> animeList anime list
     * @return void [return description]
     */
    public void setAnimeList(List<Anime> animeList) {
        this.animeList = animeList;
    }

    /**
     * Load Image
     * 
     * @param String path
     * @throws FileNotFoundException
     * @return Image
     */
    public Image loadImage(String path) throws FileNotFoundException {
        File file = new File(path);
        FileInputStream fIStream = new FileInputStream(file);
        return new Image(fIStream);
    }

    private void send(Object o) {
        try {
            os.writeObject(o);
            os.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Object receive() {
        Object received = null;
        try {
            received = is.readObject();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return received;
    }

    @Override
    public void setStream(ObjectOutputStream os, ObjectInputStream is) {
        this.os=os;
        this.is=is;
    }



}
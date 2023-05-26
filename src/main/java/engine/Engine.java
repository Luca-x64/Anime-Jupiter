package engine;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import main.Data;
import model.Anime;
import model.User;

import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.util.*;

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
    protected void addAnime(String ttl, String aut, String pub, Integer epi, Integer y, String pl, String imgPath,
            String link) throws IOException {
        Anime anime = new Anime(ttl, aut, pub, epi, y, pl, imgPath, link);
        send(3);
        send(anime);
        boolean response = (Boolean) receive();
        if (response) {
            System.out.println("Anime added!"); // DEBUG
            setExitMessagge(false,green,msgSuccess(animeAdded));
            receiveAllAnime();
        } else {
            System.out.println("Can't add Anime!"); // DEBUG
            setExitMessagge(false,red,msgWarning(animeNotAdded));
        }
    }

    protected void editAnime(int id,String ttl,String aut,String pub,int epi,int y, String pl,String imgPath, String link){
        Anime editedAnime = new Anime(ttl, aut, pub, epi, y, pl, imgPath, link);
        editedAnime.setID(id);
        send(6);
        send(editedAnime);
        boolean response = (Boolean) receive();
        if(response){
            System.out.println("Anime Edited!"); // DEBUG
            setExitMessagge(false,green,msgSuccess(animeEdited));
            receiveAllAnime();
        }else{
            System.out.println("Can't edit anime"); //DEBUG
            setExitMessagge(false,red,msgWarning(animeNotEdited));
        }
    }

    protected void setLowerStream(StreamController sc){
        sc.setStream(os, is);
    }

    protected void deleteAnime(int id) {
        send(5);
        send(id);
        Boolean response = (Boolean) receive();
        if (response) {
            System.out.println("Anime deleted!");
            receiveAllAnime();
        } else {
            System.out.println("Can't delete Anime!");
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
        displayedAnimeList = (ArrayList<Anime>) receive();
        return displayedAnimeList;
    }


    public void setExitMessagge (boolean longMessagge,Color color,String msg){
        List<Object> output = new ArrayList<>();
        output.add(longMessagge);
        output.add(color.getRed());
        output.add(color.getGreen());
        output.add(color.getBlue());
        output.add(msg);
        send(8);
        send(output);
    }

    public List<Object> getExitMessagge(){
        send(9);
        List<Object> output = (ArrayList<Object>) receive();
        return output;
    }
    /**
     * Sort Title
     * 
     * @param boolean b
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
     * @param boolean b
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

    protected void logout(){
        send(7);
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
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI uri = new URI(url);

            if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(uri);
            } else {
                System.out.println("Desktop browsing is not supported on this platform.");
                // Handle the case when desktop browsing is not supported
            }
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
        displayedAnimeList = animeList;
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
        this.os = os;
        this.is = is;
    }

}
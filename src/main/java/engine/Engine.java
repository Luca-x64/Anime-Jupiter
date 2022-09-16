package engine;

import javafx.scene.image.Image;
import main.Data;
import model.Anime;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public abstract class Engine implements Data {
    public List<Anime> animeList = new ArrayList<>();
    private final File file = new File(absoluteFilePath);

    /**
     * Engine Constructor
     */
    public Engine() {
        createFile();
    }

    /**
     * Load Anime
     *
     * @return void
     */
    public void load() {
        animeList.clear();
        animeList.addAll(readFile());
    }

    /**
     * Add Anime
     *
     * @param String ttl title
     * @param String aut author
     * @param String pub pubtor
     * @param Integer epi episodes
     * @param Integer y year
     * @param String pl plot
     * @param String imgPath image path
     * @param String link link
     * @throws IOException
     * 
     * @return void
     */
    public void addAnime(String ttl, String aut, String pub, Integer epi, Integer y, String pl, String imgPath, String link) throws IOException {
        Anime anime = new Anime(ttl, aut, pub, epi, y, pl, imgPath, link);
        animeList.add(anime);
        updateFile(anime);
    }

    /**
     * Query 
     * 
     * @param String input input field
     * @return List<Anime> query result
     */
    protected List<Anime> query(String input) {
        input = stringFormat(input);
        List<Anime> result = new ArrayList<>();

        for (Anime i : animeList) {
            String title = stringFormat(i.getTitle());
            String[] titleSplitted = title.split(" ");

            if (titleSplitted.length == 1 && title.startsWith(input)) result.add(i);
            else {
                for (String j : titleSplitted) {
                    if (j.startsWith(input)) {
                        result.add(i);
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Sort Title
     * 
     * @param boolean b
     * @return List<Anime>
     */
    public List<Anime> sortTitle(boolean b){
        List<Anime> al = new ArrayList<>(List.copyOf(animeList));
        if(b) al.sort(Comparator.comparing(Anime::getTitle));
        else al.sort(Comparator.comparing(Anime::getTitle).reversed());
        return al;
    }

    /**
     * Sort Year
     * 
     * @param boolean b
     * @return List<Anime>
     */
    public List<Anime> sortYear(boolean b){
        List<Anime> al = new ArrayList<>(List.copyOf(animeList));
        if(b) al.sort(Comparator.comparing(Anime::getYear));
        else al.sort(Comparator.comparing(Anime::getYear).reversed());
        return al;
    }

    /**
     * String Format
     * @param String s string
     * @return String
     */
    public String stringFormat(String s){
        return s.toLowerCase(Locale.ROOT).trim();
    }

    /**
     * Check Duplicates Add
     *  
     * @param List<Anime> al anime list
     * @param String ttl title
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
    public String msgSuccess(String msg){
        return "✔ "+msg +" ✔";
    }

    /**
     * Messagge Warning
     * 
     * @param String msg messagge text
     * @return String
     */
    public String msgWarning(String msg){
        return "⚠ "+msg +" ⚠";
    }

    /**
     * Messagge Danger
     * 
     * @param String msg messagge text
     * @return String
     */
    public String msgDanger(String msg){
        return "✘ "+msg +" ✘";
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
            System.out.println("Error in opening website: " + url);
        }
    }

    // CRUD operations file

    /**
     * Create file
     *
     * @return void
     */
    private void createFile() {
        try {
            if (file.createNewFile()) {
                System.out.println("File creato");
            } else load();
        } catch (Exception e) {
            System.out.println("error in creating file: " + e);
        }
    }

    /**
     * Read file
     * 
     * @return List<Anime>
     */
    private List<Anime> readFile() {
        List<Anime> loadedAnime = new ArrayList<>();
        try{
            List<String> fileBuffer = Files.readAllLines(Path.of(String.valueOf(file)));
            if (fileBuffer.size() != 0) {
                for (String i : fileBuffer) {
                    try {
                        String[] d = i.split(regex);
                        Anime anime = new Anime(d[0], d[1], d[2], Integer.parseInt(d[3]), Integer.parseInt(d[4]), d[5], d[6], d[7]);
                        loadedAnime.add(anime);
                    }catch (Exception ignored){}
                }
            }}catch (Exception ignored){}

        return loadedAnime;
    }

    /**
     * Update file
     *
     * @param Anime anime
     * @throws IOException
     * @return void
     */
    private void updateFile(Anime anime) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file.getPath(), true));
        bw.write(anime.textFormat());
        bw.close();
    }

    /**
     * Delete in file
     * @param int pos position
     * @return void
     */
    public void deleteInFile(int pos) {
        try {
            List<String> old = Files.readAllLines(Paths.get(String.valueOf(file)));
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            String toRemove = old.get(pos);
            for (String s : old) if (!s.equals(toRemove)) bw.write(s + "\n");
            bw.close();
        } catch (Exception ignored) {
        }
    }

    /**
     * Reload File
     *
     * @return void
     */
    public void reloadFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getPath()));
            for (Anime anime : animeList) bw.write(anime.textFormat());
            bw.close();
        } catch (Exception ignored) {}
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
     * @return void    [return description]
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

}
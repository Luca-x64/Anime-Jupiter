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

    public Engine() {
        createFile();
    }

    // Load Anime
    public void load() {
        animeList.clear();
        animeList.addAll(readFile());
    }

    // Add Anime
    public void addAnime(String ttl, String aut, String edi, Integer epi, Integer y, String tr, String imgPath, String link) throws IOException {
        Anime anime = new Anime(ttl, aut, edi, epi, y, tr, imgPath, link);
        animeList.add(anime);
        updateFile(anime);
    }

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

    public List<Anime> sortTitle(boolean b){
        List<Anime> al = new ArrayList<>(List.copyOf(animeList));
        if(b) al.sort(Comparator.comparing(Anime::getTitle));
        else al.sort(Comparator.comparing(Anime::getTitle).reversed());
        return al;
    }

    public List<Anime> sortYear(boolean b){
        List<Anime> al = new ArrayList<>(List.copyOf(animeList));
        if(b) al.sort(Comparator.comparing(Anime::getYear));
        else al.sort(Comparator.comparing(Anime::getYear).reversed());
        return al;
    }

    public String stringFormat(String s){
        return s.toLowerCase(Locale.ROOT).trim();
    }

    public boolean checkDuplicatesAdd(List<Anime> al, String ttl) {
        return al.stream().noneMatch(e -> stringFormat(e.getTitle()).equals(stringFormat(ttl)));
    }

    public String msgSuccess(String msg){
        return "✔ "+msg +" ✔";
    }
    public String msgWarning(String msg){
        return "⚠ "+msg +" ⚠";
    }
    public String msgDanger(String msg){
        return "✘ "+msg +" ✘";
    }
    public void openLink(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (Exception e) {
            System.out.println("Error in opening website: " + url);
        }
    }

    // CRUD operations file

    // Create file
    private void createFile() {
        try {
            if (file.createNewFile()) {
                System.out.println("File creato");
            } else load();
        } catch (Exception e) {
            System.out.println("error in creating file: " + e);
        }
    }

    // Read file
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

    // Update file
    private void updateFile(Anime anime) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file.getPath(), true));
        bw.write(anime.textFormat());
        bw.close();
    }

    // Delete in file
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

    public void reloadFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getPath()));
            for (Anime anime : animeList) bw.write(anime.textFormat());
            bw.close();
        } catch (Exception ignored) {}
    }

    // Getter & Setter
    public List<Anime> getAnimeList() {
        return animeList;
    }

    public void setAnimeList(List<Anime> animeList) {
        this.animeList = animeList;
    }

    public Image loadImage(String path) throws FileNotFoundException {
        File file = new File(path);
        FileInputStream fIStream = new FileInputStream(file);
        return new Image(fIStream);
    }

}
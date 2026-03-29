package model;

import java.io.Serializable;
import java.util.StringJoiner;

import main.Data;
import org.jetbrains.annotations.NotNull;

public class Anime implements Serializable, Data {

    //TODO attributes should not be final and should be able to be changed, why there are no setters?
    private @NotNull String title, author, publisher, plot,link,imagePath;
    private int episodes,year;
    private int ID;
    private boolean favourite = false;

    /** 
     * Anime Constructor
     *
     * @param ttl
     * @param aut
     * @param pub
     * @param epi
     * @param y
     * @param pl
     * @param imgPath
     * @param url
     */
    public Anime(@NotNull String ttl,@NotNull String aut,@NotNull String pub,int epi,int y,@NotNull String pl,@NotNull String imgPath,@NotNull String url) {
        this.title = ttl;
        this.author = aut;
        this.publisher = pub;
        this.episodes = epi;
        this.year = y;
        this.plot = pl;
        this.imagePath = imgPath;
        this.link = url;
    }
    
    /** 
     * ToString Overwritten
     *
     * @return String
     */
    @Override
    public String toString() {
        return new java.util.StringJoiner(nl)
                .add(Data.title + getTitle())
                .add(Data.author + getAuthor())
                .add(Data.publisher + getPublisher())
                .add(Data.episodes + getEpisodes())
                .add(Data.year + getYear())
                .add(Data.plot + getPlot())
                .toString();
    }

    /** 
     * Text formatted for the file
     *
     * @return String 
     */
    @Deprecated //TODO REMOVE
    public String textFormat() {
        return title + regex + author + regex + publisher + regex + episodes + regex + year + regex + plot + regex + imagePath + regex + link + nl;
    }

    // Getters

    /**
     *
     * @return String title
     */
    public String getTitle() {
        return title;
    }

    
    /**
     *
     * @return String author
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @return String publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     *
     * @return Integer number of episodes
     */
    public Integer getEpisodes() {
        return episodes;
    }

    /** 
     * Get plot
     *
     * @return String plot
     */
    public String getPlot() {
        return plot;
    }

    /** 
     * Get Year
     *
     * @return Integer year
     */
    public Integer getYear() {
        return year;
    }

    /** 
     * Get Link
     *
     * @return String link
     */
    public String getLink() {
        return link;
    }

    /** 
     * Get Image Path
     *
     * @return String image path
     */
    public String getImagePath() {
        return imagePath;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    @Deprecated //TODO is not Anime responsability
    public void favourite() {
        favourite = !favourite;
    }
    public boolean getFavourite(){
        return favourite;
    }
}
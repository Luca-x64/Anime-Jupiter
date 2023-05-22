package model;

import java.io.Serializable;

import main.Data;

public class Anime implements Serializable, Data {

    private final String title, author, publisher, plot,link,imagePath;
    private final Integer episodes,year;
    private int ID;

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
    public Anime(String ttl, String aut, String pub, Integer epi, Integer y, String pl, String imgPath, String url) {
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
        return Data.title + getTitle()
                + nl+Data.author + getAuthor()
                + nl+Data.publisher + getPublisher()
                + nl+Data.episodes + getEpisodes()
                + nl+Data.year + getYear()
                + nl+Data.plot + getPlot();
    }

    /** 
     * Text formatted for the file
     *
     * @return String 
     */
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
}
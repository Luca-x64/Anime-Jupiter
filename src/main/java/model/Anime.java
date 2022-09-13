package model;

import main.Data;

public class Anime implements Data {

    private final String title, author, publisher, plot,link,imagePath;
    private final Integer episodes,anno;

    /** Anime Constructor
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
        this.anno = y;
        this.plot = pl;
        this.imagePath = imgPath;
        this.link = url;
    }

    /** ToString Overwritten
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Titolo: " + getTitle()
                + "\nAutore: " + getAuthor()
                + "\nStudio: " + getPublisher()
                + "\nEpisodi: " + getEpisodes()
                + "\nAnno: " + getYear()
                + "\nTrama: " + getPlot();
    }

    /** Text formatted for the file
     *
     * @return String
     */
    public String textFormat() {
        return title + regex + author + regex + publisher + regex + episodes + regex + anno + regex + plot + regex + imagePath + regex + link + "\n";
    }

    // Getters

    /**
     *
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return String
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @return String
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     *
     * @return Integer
     */
    public Integer getEpisodes() {
        return episodes;
    }

    /** Get Trama
     *
     * @return String
     */
    public String getPlot() {
        return plot;
    }

    /** Get Year
     *
     * @return Integer
     */
    public Integer getYear() {
        return anno;
    }

    /** Get Link
     *
     * @return String
     */
    public String getLink() {
        return link;
    }

    /** Get Image Path
     *
     * @return String
     */
    public String getImagePath() {
        return imagePath;
    }
}
package co.empathy.academy.demo.Models;

import java.util.LinkedList;

import co.elastic.clients.util.DateTime;

//video content in the imdb_database
public class ImdbBasics {
    private String id;
    private String titleType;
    private String primaryTitle;
    private String originalTitle;
    private Boolean isAdult;
    private DateTime startYear;
    private DateTime endYear;
    private Integer runtimesMinutes;
    private LinkedList<String> genres;
     
   
    public ImdbBasics(String id, String titleType, String primaryTitle, String originalTitle, Boolean isAdult,
            DateTime startYear, DateTime endYear, Integer runtimesMinutes, LinkedList<String> genres) {
        this.id = id;
        this.titleType = titleType;
        this.primaryTitle = primaryTitle;
        this.originalTitle = originalTitle;
        this.isAdult = isAdult;
        this.startYear = startYear;
        this.endYear = endYear;
        this.runtimesMinutes = runtimesMinutes;
        this.genres = genres;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitleType() {
        return titleType;
    }
    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }
    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Boolean getIsAdult() {
        return isAdult;
    }
    public void setIsAdult(Boolean isAdult) {
        this.isAdult = isAdult;
    }
    public DateTime getStartYear() {
        return startYear;
    }
    public void setStartYear(DateTime startYear) {
        this.startYear = startYear;
    }
    public DateTime getEndYear() {
        return endYear;
    }
    public void setEndYear(DateTime endYear) {
        this.endYear = endYear;
    }
    public Integer getRuntimesMinutes() {
        return runtimesMinutes;
    }
    public void setRuntimesMinutes(Integer runtimesMinutes) {
        this.runtimesMinutes = runtimesMinutes;
    }
    public LinkedList<String> getGenres() {
        return genres;
    }
    public void setGenres(LinkedList<String> genres) {
        this.genres = genres;
    }
}

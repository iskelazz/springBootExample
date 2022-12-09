package co.empathy.academy.demo.Models;

import java.util.List;

//import java.util.LinkedList; Falta el campo genre que es una lista

import lombok.*;

//import co.elastic.clients.util.DateTime;

//video content in the imdb_database

@With
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Value
public class Movie {
    private String id;
    private String titleType;
    private String primaryTitle;
    private String originalTitle;
    private Boolean isAdult;
    private Integer startYear;
    private String endYear;
    private Integer runtimeMinutes;
    private List<String> genres;  
    private Double averageRating;
    private Integer numVotes;
    private List<Aka> akas;
    private List<Director> directors;
    private List<Starring> starring;
}

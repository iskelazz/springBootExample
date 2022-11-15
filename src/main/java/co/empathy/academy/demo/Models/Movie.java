package co.empathy.academy.demo.Models;

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
    private String startYear;
    private String endYear;
    private Integer runtimesMinutes;
    private String genre;  
    private Double averageRating;
    private int numVotes;
}

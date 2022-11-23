package co.empathy.academy.demo.Models;

import java.util.List;

import lombok.*;

//import co.elastic.clients.util.DateTime;

//video content in the imdb_database

@With
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Value
public class hits {
    List<Movie> hits;
}

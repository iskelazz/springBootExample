package co.empathy.academy.demo.DAOs;

import java.util.List;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.empathy.academy.demo.Models.Movie;

public interface SearchDataAccess {
    void bulk (List<Movie> movies, String index) throws Exception;
    void mapping(String index, String mapping) throws Exception;
    void analyzer (String index, String mapping) throws Exception;
    Query multiMatchQuery (String query, String[] fields);
    Query queryTerms(String[] values, String field);
    Query queryTerm (String value, String field);
    List<Movie>throwQuery(Query query, String index);
}

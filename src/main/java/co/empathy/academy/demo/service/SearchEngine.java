package co.empathy.academy.demo.service;

import java.util.List;

import co.empathy.academy.demo.Models.Movie;

public interface SearchEngine {

    String search(String query) throws Exception;
    String search(String index, String Body) throws Exception;
    String getVersion() throws Exception;
    String getIndex() throws Exception;
    String putIndex(String index) throws Exception;
    String putIndex(String index, String Body) throws Exception;
    String addDocument (String index, Movie Body) throws Exception;
    String postDocuments (String index, String id, String Body) throws Exception;
    void mapping (String index, String mapping) throws Exception;
    String bulk (List<Movie> movies) throws Exception;
}

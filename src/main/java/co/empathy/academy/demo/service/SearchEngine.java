package co.empathy.academy.demo.service;

import co.empathy.academy.demo.Models.Movie;

public interface SearchEngine {

    String search(String query) throws Exception;
    String search(String index, String Body) throws Exception;
    String getVersion() throws Exception;
    String getIndex() throws Exception;
    String addDocument (String index, Movie Body) throws Exception;
    String postDocuments (String index, String id, String Body) throws Exception;
}

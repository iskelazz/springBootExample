package co.empathy.academy.demo.service;

import co.empathy.academy.demo.Models.Movie;

public interface SearchService {

    String search(String query) throws Exception;
    String search(String index, String body) throws Exception;
    String getVersion() throws Exception;
    String getIndex() throws Exception;
    String putIndex(String index) throws Exception;
    String putIndex(String index, String body) throws Exception;
    String postDocuments(String index, Movie body) throws Exception;
    String postDocuments(String index, String id, String body) throws Exception;
    void mapping(String index, String mapping) throws Exception;
    void indexDatabase() throws Exception;

}

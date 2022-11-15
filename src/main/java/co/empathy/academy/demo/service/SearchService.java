package co.empathy.academy.demo.service;

import java.io.IOException;
import java.util.List;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.empathy.academy.demo.Models.Movie;

public interface SearchService {

    String search(String query) throws Exception;
    String search(String index, String body) throws Exception;
    List<Movie> multiMatchSearch(String query, String fields, String index) throws ElasticsearchException, IOException;
    List<Movie> queryTermSearch (String query, String field, String index) throws ElasticsearchException, IOException;
    List<Movie> queryTermsSearch(String query[], String field, String index) throws ElasticsearchException, IOException;
    String getVersion() throws Exception;
    String getIndex() throws Exception;
    String putIndex(String index) throws Exception;
    String putIndex(String index, String body) throws Exception;
    String postDocuments(String index, Movie body) throws Exception;
    String postDocuments(String index, String id, String body) throws Exception;
    void mapping(String index, String mapping) throws Exception;
    void analyzer(String index, String mapping) throws Exception;
    List<Movie> startYearFilter(String index,String startYear) throws Exception;
    void indexDatabase() throws Exception; 


}

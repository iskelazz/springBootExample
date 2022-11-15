package co.empathy.academy.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.empathy.academy.demo.DAOs.SearchDataAccess;
import co.empathy.academy.demo.Models.Movie;
import co.empathy.academy.demo.util.ReaderTSV;
import co.empathy.academy.demo.util.InputValidationException;
import co.empathy.academy.demo.util.Validator;

// This is the service class that will implement your search service logic
// It has a SearchEngine as a dependency
// Endpoint: /search (controller) -> SearchService -> SearchEngine

public class SearchServiceElastic implements SearchService {

    @Autowired
    private SearchEngine elasticEngine;
    private SearchDataAccess elasticClient; 

    public SearchServiceElastic(SearchEngine elasticEngine, SearchDataAccess elasticClient){
        this.elasticEngine = elasticEngine;
        this.elasticClient = elasticClient;
    }
    
    @Override
    public String search(String query) throws Exception {
        return elasticEngine.search(query);
    }
    @Override
    public String search(String index, String body) throws Exception {
        return elasticEngine.search(index, body);
    }

    @Override
    public List<Movie> multiMatchSearch(String query, String fields, String index) throws ElasticsearchException, IOException {
        String[] fieldsArray = fields.split(",");
        return elasticClient.throwQuery(elasticClient.multiMatchQuery(query, fieldsArray), index);
    }

    @Override
    public List<Movie> queryTermSearch(String query, String field, String index) throws ElasticsearchException, IOException{
        return elasticClient.throwQuery(elasticClient.queryTerm(query,field), index);
    }

    @Override
    public List<Movie> queryTermsSearch(String query[], String field, String index) throws ElasticsearchException, IOException{
        return elasticClient.throwQuery(elasticClient.queryTerms(query,field), index);
    }

    @Override
    public String getVersion() throws Exception {
        return elasticEngine.getVersion();
    }
    @Override
    public String getIndex() throws Exception {
        return elasticEngine.getIndex();
    }
    @Override
    public String putIndex(String index) throws Exception {
        return elasticEngine.putIndex(index);
    }
    @Override
    public String putIndex(String index, String body) throws Exception {
        return elasticEngine.putIndex(index, body);
    }
    @Override
    public String postDocuments(String index, Movie body) throws Exception {
        validateMovie(body);
        return elasticEngine.addDocument(index,body);
    }
    @Override
    public String postDocuments(String index, String id, String body) throws Exception {
        return elasticEngine.postDocuments(index, id, body);
    }

    @Override
    public void mapping (String index, String mapping) throws Exception{
        elasticClient.mapping(index, mapping);
    }

    @Override
    public void analyzer(String index, String analyzer) throws Exception {
        elasticClient.analyzer(index, analyzer);
        
    }

    //Basics y Ratings
    @Override
    public void indexDatabase() throws Exception{
        File basics = new File("/Users/alejandrorg/title.basics.tsv");
        File ratings = new File("/Users/alejandrorg/title.ratings.tsv");
        ReaderTSV reader = new ReaderTSV(basics,ratings);
        LinkedList<Movie> bulk = new LinkedList<>();
        System.out.println(reader.extractHeadersBasics());
        System.out.println(reader.extractHeadersRatings());
        while(!reader.getFinished()){ 
            bulk = reader.tsvToMovies();
            elasticClient.bulk(bulk,"simba");
        }
    }

    public void validateMovie(Movie movie) throws InputValidationException{
        Validator.validateMandatoryString("title type", movie.getTitleType());
        
            //Validate all fields. In production
    }

    //By default, order by numVotes
    @Override
    public List<Movie> startYearFilter(String index, String startYear) throws Exception {
        Map<String, Aggregation> map = elasticClient.orderBy("numVotes", SortOrder.Desc);
        return elasticClient.throwOrderByQuery(elasticClient.queryTerm(startYear,"startYear"),map, index, "numVotes");
    }

    
    
}

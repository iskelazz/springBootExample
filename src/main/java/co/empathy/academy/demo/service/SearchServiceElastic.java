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
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.cat.IndicesResponse;
import co.empathy.academy.demo.DAOs.SearchDataAccess;
import co.empathy.academy.demo.Models.Movie;
import co.empathy.academy.demo.util.ReaderTSV;
import co.empathy.academy.demo.util.InputValidationException;
import co.empathy.academy.demo.util.Validator;

// This is the service class that will implement your search service logic
// It has a SearchEngine as a dependency
// Endpoint: /search (controller) -> SearchService -> SearchEngine

public class SearchServiceElastic implements SearchService {

    private static final int INT_MAX = 2000000000;
    private static final int MAX_VALUE_QUERY = 999999;

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
    public IndicesResponse getIndex() throws Exception {
        return elasticClient.getIndex();
    }
    @Override
    public void putIndex(String index) throws Exception {
        elasticClient.putIndex(index);
    }
   
    @Override
    public String postDocuments(String index, Movie body) throws Exception {
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
        File akas = new File("/Users/alejandrorg/title.akas.tsv");
        File crew = new File("/Users/alejandrorg/title.crew.tsv");
        File principals = new File("/Users/alejandrorg/title.principals.tsv");
        ReaderTSV reader = new ReaderTSV(basics,ratings, akas, crew, principals);
        LinkedList<Movie> bulk = new LinkedList<>();
        System.out.println(reader.extractHeadersBasics());
        System.out.println(reader.extractHeadersRatings());
        System.out.println(reader.extractHeadersAkas());
        System.out.println(reader.extractHeadersCrew());
        System.out.println(reader.extractHeadersPrincipals());


        while(!reader.getFinished()){ 
            bulk = reader.tsvToMovies();
            elasticClient.bulk(bulk,"simba");
        }
    }

    public void validateMovie(Movie movie) throws InputValidationException{
        Validator.validateMandatoryString("title type", movie.getTitleType());
        
            //Validate all fields. In production
    }

    //Filters
    //By default, order by numVotes
    

    @Override
    public List<Movie> maxAverageRating (String index) throws ElasticsearchException, IOException{
        Map<String, Aggregation> map = elasticClient.orderBy("averageRating", SortOrder.Desc,25);
        return elasticClient.throwOrderByQuery(elasticClient.numericFilter("numVotes",30000,INT_MAX),map, index, "averageRating");
    }

    @Override
    public List<Movie> minAverageRating (String index) throws ElasticsearchException, IOException{
        Map<String, Aggregation> map = elasticClient.orderBy("averageRating", SortOrder.Asc,25);
        return elasticClient.throwOrderByQuery(elasticClient.numericFilter("numVotes",30000,INT_MAX,0.1),map, index, "averageRating");
    }

    public List<Query> genreFilter (String index, String[] genre) throws Exception {
        List<Query> List_genre = new LinkedList<>();
        for (int i = 0; i<genre.length;i++){
            List_genre.add(elasticClient.matchQuery(genre[i],"genre"));
        }
        return List_genre;
    }

    public Query rangeYearFilter (Integer minYear, Integer maxYear) throws Exception {
        if (minYear == null) minYear = 1;
        if (maxYear == null) maxYear = MAX_VALUE_QUERY;
        return elasticClient.numericFilter("startYear",minYear,maxYear);
    }
    
    public Query runtimeFilter (Integer minValue, Integer maxValue) throws Exception {
        if (minValue == null) minValue = 0;
        if (maxValue == null) maxValue = MAX_VALUE_QUERY;
        return elasticClient.numericFilter("runtimesMinutes",minValue,maxValue);
    }

    public Query averageRatingFilter (Float minRating, Float maxRating) throws Exception {
        if (minRating == null) minRating = 0f;
        if (maxRating == null) maxRating = 10f;

        return elasticClient.numericFilter("averageRating",minRating,maxRating);
    }

    public Query typeFilter(String type) throws Exception {
        return elasticClient.queryTerm(type, "titleType");
    }

    @Override
    public List<Movie> processParam(String index, String[] genre, Integer minYear, Integer maxYear, 
    Integer minMinutes, Integer maxMinutes, Float minRating, Float maxRating, String type, Integer nhits) throws Exception {
        Map<String, Aggregation> map;
        if (nhits != null) map = elasticClient.orderBy("numVotes", SortOrder.Desc,nhits);
        else map = elasticClient.orderBy("numVotes", SortOrder.Desc,100);
        
        
        List<Query> List_queries = new LinkedList<>();
        if (genre != null){
            List_queries.addAll(genreFilter(index,genre));
        }
        if ((maxYear != null) || (minYear!=null)){
            List_queries.add(rangeYearFilter(minYear, maxYear));
        }
        if ((minMinutes!=null)||(maxMinutes!=null)){
            List_queries.add(runtimeFilter(minMinutes,maxMinutes));
        }
        if ((minRating!=null)||(maxRating!=null)){
            List_queries.add(averageRatingFilter(minRating,maxRating));
        }
        if ((type!=null) && ((type.equals("movie")) || (type.equals("tvSeries")))){
            List_queries.add(typeFilter(type));
        }
            
        return elasticClient.throwOrderByQuery(elasticClient.must(List_queries),map, index, "numVotes"); 
    }
    
    
}

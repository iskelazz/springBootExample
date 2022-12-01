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
    private static final int MIN_VOTES = 15000;

    @Autowired
    private SearchEngine elasticEngine;
    private SearchDataAccess elasticClient; 

    public SearchServiceElastic(SearchEngine elasticEngine, SearchDataAccess elasticClient){
        this.elasticEngine = elasticEngine;
        this.elasticClient = elasticClient;
    }
    
    @Override
    public List<Movie> search(String query, String index) throws Exception {
        return elasticClient.throwQuery(elasticClient.matchQuery(query,"originalTitle"), index);
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
    public void postDocument(String index, Movie body) throws Exception {
        elasticClient.addDocument(index,body);
    }
    @Override
    public void postDocument(String index, String id, Movie body) throws Exception {
        elasticClient.addDocument(index, id, body);
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
    public void indexDatabase(String file_basics, String file_ratings, String akas_file, 
    String crew_file, String principals_file) throws Exception{
        // /Users/alejandrorg/title.basics.tsv
        File basics = new File(file_basics);
        // /Users/alejandrorg/title.ratings.tsv
        File ratings = new File(file_ratings);
        // /Users/alejandrorg/title.akas.tsv
        File akas = new File(akas_file);
        // /Users/alejandrorg/title.crew.tsv
        File crew = new File(crew_file);
        // /Users/alejandrorg/title.principals.tsv
        File principals = new File(principals_file);
        ReaderTSV reader = new ReaderTSV(basics,ratings, akas, crew, principals);
        LinkedList<Movie> bulk = new LinkedList<>();

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
    public Query numVotesFilter (Integer minVotes) throws Exception{
        return elasticClient.numericFilter("numVotes", minVotes);
    }

    public Query typeFilter(String type) throws Exception {
        return elasticClient.queryTerm(type, "titleType");
    }

    @Override
    public List<Movie> processParam(String index, String[] genre, Integer minYear, Integer maxYear, String sortRating,
    Integer minMinutes, Integer maxMinutes, Float minRating, Float maxRating, String type, Integer nhits) throws Exception {
        Map<String, Aggregation> map;
        List<Query> List_queries = new LinkedList<>();
        String key = "numVotes";
        if (sortRating == null){    
            if (nhits != null) map = elasticClient.orderBy(key, SortOrder.Desc,nhits);
            else map = elasticClient.orderBy(key, SortOrder.Desc,100);            
        }
        else {
            key = "averageRating";
            SortOrder order;
            if (sortRating.equals("asc")) order = SortOrder.Asc;
            else order = SortOrder.Desc; 
            if (nhits != null) map = elasticClient.orderBy(key, order,nhits);
            else map = elasticClient.orderBy(key, order,10);
            List_queries.add(numVotesFilter(MIN_VOTES));
        }
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
            
        return elasticClient.throwOrderByQuery(elasticClient.must(List_queries),map, index, key); 
    }
    
    
}

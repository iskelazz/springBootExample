package co.empathy.academy.demo.DAOs;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.cat.IndicesResponse;
import co.empathy.academy.demo.Models.Movie;

public interface SearchDataAccess {
    void bulk (List<Movie> movies, String index) throws Exception;
    void mapping(String index, String mapping) throws Exception;
    void analyzer (String index, String mapping) throws Exception;
    
    void putIndex(String index) throws IOException;
    IndicesResponse getIndex() throws ElasticsearchException, IOException;
    
    Query multiMatchQuery (String query, String[] fields);
    Query queryTerms(String[] values, String field);
    Query queryTerm (String value, String field);
    List<Movie>throwQuery(Query query, String index) throws ElasticsearchException, IOException;    
    List<Movie> throwOrderByQuery(Query query,Map<String,Aggregation> aggs, String index, String key) throws ElasticsearchException, IOException;
    Map<String, Aggregation> orderBy(String key, SortOrder sort, int nhits);

    Query numericFilter (String key, int minValue, int maxValue);
    Query numericFilter (String key, double minValue, double maxValue);
    Query numericFilter (String key, int minValue, int maxValue, double minRating);
    Query matchQuery (String match, String field);
    
    Query must (List<Query> queries);
    Query should (List<Query> queries);
}

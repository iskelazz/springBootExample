package co.empathy.academy.demo.DAOs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch.cat.IndicesResponse;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.empathy.academy.demo.Models.Movie;
import co.empathy.academy.demo.util.JsonConversor;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;

public class SearchDataAccessImp implements SearchDataAccess{
    private ElasticsearchClient esClient;

    public SearchDataAccessImp(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    @Override
    public void bulk(List<Movie> movies, String index) throws Exception {
        BulkRequest.Builder request = new BulkRequest.Builder();
        movies.forEach(movie -> request.operations(op -> op
                .index(i -> i
                        .index(index)
                        .id(movie.getId())
                        .document(movie))));

        try {
            co.elastic.clients.elasticsearch.core.BulkResponse result = esClient.bulk(request.build());
            if (result.errors()){
                System.out.println(result.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //PUT mapping for a index that exists
    @Override
    public void mapping(String index, String mapping) throws Exception {
        InputStream inputMapping = new ByteArrayInputStream(mapping.getBytes()); 
        esClient.indices().putMapping(p -> p.index(index).withJson(inputMapping));    
    }
    
    //adds an index to the database (without body)
    @Override
    public void putIndex(String index) throws IOException {
        try {
            esClient.indices().delete(d -> d.index(index));
        } catch (Exception e) {
            
        }

        esClient.indices().create(c -> c.index(index));
    }

    @Override
    public IndicesResponse getIndex() throws ElasticsearchException, IOException{
        return esClient.cat().indices();
        //return esClient.cat().indices().toString();
    }

    @Override
    public Query queryTerm(String value, String field) {
        Query termQuery = TermQuery.of(t -> t
                .value(value)
                .field(field))._toQuery();
        return termQuery;
    }

    @Override
    public Query queryTerms(String[] values, String field) {
        TermsQueryField termsQueryField = TermsQueryField.of(t -> t
                .value(Arrays.stream(values).toList().stream().map(FieldValue::of).collect(Collectors.toList())));

        Query termsQuery = TermsQuery.of(t -> t
                .field(field)
                .terms(termsQueryField))._toQuery();

        return termsQuery;
    }

    @Override
    public Query multiMatchQuery(String query, String[] fields) {
        Query multiMatchQuery = MultiMatchQuery.of(m -> m
                .query(query)
                .fields(Arrays.stream(fields).toList()))._toQuery();

        return multiMatchQuery;
    }

    @Override
    public List<Movie> throwQuery(Query query, String index, int nhits) throws ElasticsearchException, IOException {
        SearchResponse<Movie> response = esClient.search(s -> s
        .index(index)
        .query(query)
        .size(nhits), Movie.class);

        return response.hits().hits().stream()
        .map(Hit::source)
        .toList();
    }

    @Override
    public void analyzer(String index, String analyzer) throws Exception {
        esClient.indices().close(c -> c.index(index));

        InputStream inputAnalyzer = new ByteArrayInputStream(analyzer.getBytes()); 
        esClient.indices().putSettings(p -> p.index(index).withJson(inputAnalyzer));

        esClient.indices().open(o -> o.index(index));
        
    }

    @Override
    public Query matchQuery (String match, String field){
        Query termQuery = MatchQuery.of(t -> t.field(field).query(match))._toQuery();
        return termQuery;
    }

    @Override
    public Query must (List<Query> queries){
        return BoolQuery.of(t -> t.must(queries))._toQuery();  
        
    }

    @Override
    public Query should (List<Query> queries){
        return BoolQuery.of(t -> t.should(queries))._toQuery();  
        
    }

    @Override
    public Query numericFilter (String key, int minValue, int maxValue){
        Query numericFilter = RangeQuery.of(t -> t.field(key)
            .lte(JsonData.of(maxValue))
            .gte(JsonData.of(minValue)))  
            ._toQuery();
        return numericFilter;
    }

    @Override
    public Query numericFilter (String key, double minValue, double maxValue){
        Query numericFilter = RangeQuery.of(t -> t.field(key)
            .lte(JsonData.of(maxValue))
            .gte(JsonData.of(minValue)))  
            ._toQuery();
        return numericFilter;
    }

    //Better use other name for doing it diferent than others numericFilter with range max/min
    @Override
    public Query numericFilter (String key, Integer minValue){
        Query numericFilter = RangeQuery.of(t -> t.field(key)
            .gte(JsonData.of(minValue)))  
            ._toQuery();
        return numericFilter;
    }

    //Numeric filter with rating min
    @Override
    public Query numericFilter (String key, int minValue, int maxValue, double minRating){
        Query numericFilter = RangeQuery.of(t -> t.field(key)
            .lte(JsonData.of(maxValue))
            .gte(JsonData.of(minValue)))  
            ._toQuery();
        Query ratingFilter = RangeQuery.of(t -> t.field("averageRating")
        .gte(JsonData.of(minRating)))
        ._toQuery();
        LinkedList<Query> queries = new LinkedList<>();
        queries.add(numericFilter);
        queries.add(ratingFilter);
        Query must = BoolQuery.of(t -> t.must(queries))._toQuery();    
        Query result = BoolQuery.of(t -> t.filter(must))._toQuery();
        return result;
    }

    @Override
    public List<Movie> throwOrderByQuery(Query query, Map<String,Aggregation> aggs, String index, String key) throws ElasticsearchException, IOException {
        SearchResponse<Movie> response = esClient.search(s -> s
            .index(index)
            .size(0)
            .query(query)
            .aggregations(aggs), Movie.class);

        Aggregate Aggregate = response.aggregations().get(key);
                
        List<JsonData> result = Aggregate.topHits().hits().hits().stream()
            .map(Hit::source)
            .toList();

        JSONArray jsonArray = new JSONArray(result.toString());
        return JsonConversor.jsontoMovies(jsonArray); 
    }
    
    //In the future size by parameter, meanwhile it value will be 25
    @Override
    public Map<String, Aggregation> orderBy(String key, SortOrder sort, int nhits){
        Map<String, Aggregation> map = new HashMap<String, Aggregation>();
        FieldSort order = FieldSort.of(h -> h.field(key).order(sort));
        Aggregation aggregate = TopHitsAggregation.of(t -> t.size(nhits).sort(SortOptions.of(h -> h.field(order))))._toAggregation();
        map.put(key, aggregate);
        return map;
    }

    @Override
    public void addDocument(String index, Movie movie) throws Exception {
        esClient.index(i -> i
                .index(index)
                .id(movie.getId())
                .document(movie));
        
    }

    @Override
    public void addDocument(String index,String id, Movie movie) throws Exception {
        esClient.index(i -> i
                .index(index)
                .id(id)
                .document(movie));
        
    }

    /* 
    Test with aggregate
    @Override
    public List<Movie> aggs(Query query) throws ElasticsearchException, IOException {
        Map<String, Aggregation> map = new HashMap<String, Aggregation>();
        FieldSort order = FieldSort.of(h -> h.field("numVotes").order(SortOrder.Desc));
        Aggregation titleType = TopHitsAggregation.of(t -> t.size(5).sort(SortOptions.of(h -> h.field(order))))._toAggregation();
        map.put("numVotes", titleType);
         SearchResponse<Movie> response = esClient.search(b -> b
         .index("simba")
         .size(0)
         .query(query)
         .aggregations(map), Movie.class);
         Aggregate genresAgg = response.aggregations().get("numVotes");
                
        List<JsonData> result = genresAgg.topHits().hits().hits().stream()
        .map(Hit::source)
        .toList();

        JSONArray jsonArray = new JSONArray(result.toString());
        return JsonConversor.jsontoMovies(jsonArray); 
        
    }*/
    
    
}

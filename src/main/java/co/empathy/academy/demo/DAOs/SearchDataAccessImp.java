package co.empathy.academy.demo.DAOs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.empathy.academy.demo.Models.Movie;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;

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
                System.out.println("El indexado fallo");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void mapping(String index, String mapping) throws Exception {
        InputStream inputMapping = new ByteArrayInputStream(mapping.getBytes()); 
        esClient.indices().putMapping(p -> p.index(index).withJson(inputMapping));    
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
    public List<Movie> throwQuery(Query query, String index) {
        try {
            SearchResponse<Movie> response = esClient.search(s -> s
                    .index(index)
                    .query(query), Movie.class);

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void analyzer(String index, String analyzer) throws Exception {
        esClient.indices().close(c -> c.index(index));

        InputStream inputAnalyzer = new ByteArrayInputStream(analyzer.getBytes()); 
        esClient.indices().putSettings(p -> p.index(index).withJson(inputAnalyzer));

        esClient.indices().open(o -> o.index(index));
        
    }
    
    
}

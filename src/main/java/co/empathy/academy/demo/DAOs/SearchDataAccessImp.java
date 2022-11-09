package co.empathy.academy.demo.DAOs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.empathy.academy.demo.Models.Movie;
import co.elastic.clients.elasticsearch.core.*;

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
    public void mapping(String index, String mapping) throws IOException {
        InputStream inputMapping = new ByteArrayInputStream(mapping.getBytes()); 
        esClient.indices().putMapping(p -> p.index(index).withJson(inputMapping));    
        
    }

    
    
}

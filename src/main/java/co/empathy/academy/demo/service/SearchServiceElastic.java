package co.empathy.academy.demo.service;

import co.empathy.academy.demo.DAOs.SearchEngine;

// This is the service class that will implement your search service logic
// It has a SearchEngine as a dependency
// Endpoint: /search (controller) -> SearchService -> SearchEngine

public class SearchServiceElastic implements SearchService {

    private SearchEngine elasticEngine;

    public SearchServiceElastic(SearchEngine elasticEngine){
        this.elasticEngine = elasticEngine;
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
    public String postDocuments(String index, String body) throws Exception {
        return elasticEngine.postDocuments(index, body);
    }
    @Override
    public String postDocuments(String index, String id, String body) throws Exception {
        return elasticEngine.postDocuments(index, id, body);
    }
    
    
}

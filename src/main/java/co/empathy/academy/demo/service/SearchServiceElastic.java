package co.empathy.academy.demo.service;

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
    public String isUp() throws Exception {
        return elasticEngine.isUp();
    }
    
}

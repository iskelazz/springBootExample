package co.empathy.academy.demo.configuration;

import co.empathy.academy.demo.DAOs.SearchDataAccess;
import co.empathy.academy.demo.DAOs.SearchDataAccessImp;
import co.empathy.academy.demo.service.SearchEngine;
import co.empathy.academy.demo.service.SearchEngineElastic;
import co.empathy.academy.demo.service.SearchService;
import co.empathy.academy.demo.service.SearchServiceElastic;
import co.elastic.clients.elasticsearch.ElasticsearchClient;

import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// This is the configuration class where we'll define our beans (objects whose lifecycle is managed by Spring)
@Configuration
public class Config {

    @Bean
    public SearchEngine searchEngine(RestClient restClient) {
        return new SearchEngineElastic(restClient);
    }

    @Bean 
    public SearchDataAccess SearchDataAccess(ElasticsearchClient elasticsearch){
        return new SearchDataAccessImp(elasticsearch);
    }

    /*@Bean
    public SearchService searchService(SearchEngine searchEngine) {
        return new SearchServiceElastic(searchEngine);
    }*/

    @Bean 
    public SearchService searchService(SearchEngine searchEngine, SearchDataAccess searchdata){
        return new SearchServiceElastic(searchEngine,searchdata);
    }

}

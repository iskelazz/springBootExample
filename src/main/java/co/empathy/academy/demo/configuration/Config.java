package co.empathy.academy.demo.configuration;

import co.empathy.academy.demo.DAOs.SearchEngine;
import co.empathy.academy.demo.DAOs.SearchEngineElastic;
import co.empathy.academy.demo.service.SearchService;
import co.empathy.academy.demo.service.SearchServiceElastic;

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
    public SearchService searchService(SearchEngine searchEngine) {
        return new SearchServiceElastic(searchEngine);
    }
}

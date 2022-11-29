package co.empathy.academy.demo.controller;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import co.empathy.academy.demo.service.SearchService;

@ExtendWith(MockitoExtension.class)
class SearchServiceControllerTest {

    @Test
    void givenQueryWithResults_whengetVersion_thenReturnString() throws Exception {
        SearchService searchService = mock(SearchService.class);
        given(searchService.getVersion()).willReturn("\"tagline\": \"You Know, for Search\"");

        SearchController searchController = new SearchController(searchService);

        String queryResults = searchController.getVersion();

        assertTrue(queryResults == "\"tagline\": \"You Know, for Search\"");
    }


  /*   @Test
    void givenQueryWithResults_whenpostDocuments_thenReturnString() throws Exception {
        String index = "samples";
        String body = "{\"peli\":\"Ant-man\"}";
        SearchService searchService = mock(SearchService.class);
        given(searchService.postDocuments(index,body)).willReturn("{\"result\": \"created\"}");
        SearchController searchController = new SearchController(searchService);
        String queryResults = searchController.postDocuments(index,body);
        assertTrue(queryResults == "{\"result\": \"created\"}");
    }*/
    
}
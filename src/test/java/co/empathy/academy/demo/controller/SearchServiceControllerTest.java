package co.empathy.academy.demo.controller;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void givenQueryWithResults_whenSearch_thenReturnString() throws Exception {
        String query = "query with results";
        SearchService searchService = mock(SearchService.class);
        given(searchService.search(query)).willReturn("\"Frutas\": \"Manzana\"");

        SearchController searchController = new SearchController(searchService);

        String queryResults = searchController.search(query);

        assertTrue(queryResults == "\"Frutas\": \"Manzana\"");
    }

    @Test
    void givenQueryWithResults_whenSearchbody_thenReturnString() throws Exception {
        String index = "croma";
        String body = "{\"peli\": \"all quiet on the\"}";
        SearchService searchService = mock(SearchService.class);
        given(searchService.search(index,body)).willReturn("{\"source\": {\"peli\":\"All quiet on the western front\"}}");

        SearchController searchController = new SearchController(searchService);

        String queryResults = searchController.search(index,body);

        assertTrue(queryResults == "{\"source\": {\"peli\":\"All quiet on the western front\"}}");
    }

    @Test
    void givenQueryWithResults_whengetVersion_thenReturnString() throws Exception {
        SearchService searchService = mock(SearchService.class);
        given(searchService.getVersion()).willReturn("\"tagline\": \"You Know, for Search\"");

        SearchController searchController = new SearchController(searchService);

        String queryResults = searchController.getVersion();

        assertTrue(queryResults == "\"tagline\": \"You Know, for Search\"");
    }

    @Test
    void givenQueryWithResults_whengetIndex_thenReturnString() throws Exception {
        SearchService searchService = mock(SearchService.class);
        given(searchService.getIndex()).willReturn("\"tagline\": \"You Know, for Search\"");
        SearchController searchController = new SearchController(searchService);
        String queryResults = searchController.getIndex();
        assertTrue(queryResults == "\"tagline\": \"You Know, for Search\"");
    }

    @Test
    void givenQueryWithResults_whenputIndex_thenReturnString() throws Exception {
        String index = "samples";
        SearchService searchService = mock(SearchService.class);
        given(searchService.putIndex(index)).willReturn("{\"acknowledged\": \"true\"}");
        SearchController searchController = new SearchController(searchService);
        String queryResults = searchController.putIndex(index,null);
        assertTrue(queryResults == "{\"acknowledged\": \"true\"}");
    }

    @Test
    void givenQueryWithResults_whenputIndexwithBody_thenReturnString() throws Exception {
        String index = "samples";
        String body = "{\"colors\":\"grey\"}";
        SearchService searchService = mock(SearchService.class);
        given(searchService.putIndex(index,body)).willReturn("{\"shards_acknowledged\": \"true\"}");
        SearchController searchController = new SearchController(searchService);
        String queryResults = searchController.putIndex(index,body);
        assertTrue(queryResults == "{\"shards_acknowledged\": \"true\"}");
    }
    @Test
    void givenQueryWithResults_whenpostDocuments_thenReturnString() throws Exception {
        String index = "samples";
        String body = "{\"peli\":\"Ant-man\"}";
        SearchService searchService = mock(SearchService.class);
        given(searchService.postDocuments(index,body)).willReturn("{\"result\": \"created\"}");
        SearchController searchController = new SearchController(searchService);
        String queryResults = searchController.postDocuments(index,body);
        assertTrue(queryResults == "{\"result\": \"created\"}");
    }
    
    @Test
    void givenQueryWithResults_whenpostDocumentswithId_thenReturnString() throws Exception {
        String index = "samples";
        String body = "{\"peli\":\"Ant-man\"}";
        String id = "14";
        SearchService searchService = mock(SearchService.class);
        given(searchService.postDocuments(index,body,id)).willReturn("{\"result\": \"created\"}");
        SearchController searchController = new SearchController(searchService);
        String queryResults = searchController.postDocuments(index,body,id);
        assertTrue(queryResults == "{\"result\": \"created\"}");
    }

    @Test
    void givenNoQuery_whenSearch_thenPropagateError() throws Exception {
        SearchService searchService = mock(SearchService.class);
        Throwable expectedException = new Exception("Error while searching");
        given(searchService.search(null)).willThrow(expectedException);

        SearchController searchController = new SearchController(searchService);

        assertThrows(expectedException.getClass(), () -> searchController.search(null));
    }
}
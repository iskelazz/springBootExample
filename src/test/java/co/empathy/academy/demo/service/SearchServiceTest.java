package co.empathy.academy.demo.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import co.empathy.academy.demo.DAOs.SearchDataAccess;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Test
    void givenQueryWithResults_whenSearch_thenReturnString() throws Exception {
        String query = "query with results";
        SearchEngine searchEngine = mock(SearchEngine.class);
        given(searchEngine.search(query)).willReturn("Jane");
        SearchDataAccess Searchclient = mock(SearchDataAccess.class);;
        SearchService searchService = new SearchServiceElastic(searchEngine,Searchclient);

        String queryResults = searchService.search(query);

        assertTrue(queryResults == "Jane");
    }

    @Test
    void givenQueryWithResults_whengetVersion_thenReturnString() throws Exception {
        SearchEngine searchEngine = mock(SearchEngine.class);
        given(searchEngine.getVersion()).willReturn("You Know, for Search");
        SearchDataAccess Searchclient = mock(SearchDataAccess.class);

        SearchService searchService = new SearchServiceElastic(searchEngine,Searchclient);

        String queryResults = searchService.getVersion();

        assertTrue(queryResults == "You Know, for Search");
    }

    @Test
    void givenQueryWithResults_whenSearchbody_thenReturnString() throws Exception {
        String index = "croma";
        String body = "{\"peli\": \"all quiet on the\"}";
        SearchEngine searchEngine = mock(SearchEngine.class);
        given(searchEngine.search(index,body)).willReturn("{\"source\": {\"peli\":\"All quiet on the western front\"}}");
        SearchDataAccess Searchclient = mock(SearchDataAccess.class);

        SearchService searchService = new SearchServiceElastic(searchEngine,Searchclient);

        String queryResults = searchService.search(index,body);

        assertTrue(queryResults == "{\"source\": {\"peli\":\"All quiet on the western front\"}}");
    }

    @Test
    void givenQueryWithResults_whengetIndex_thenReturnString() throws Exception {
        SearchEngine searchEngine = mock(SearchEngine.class);
        given(searchEngine.getIndex()).willReturn("\"tagline\": \"You Know, for Search\"");
        SearchDataAccess Searchclient = mock(SearchDataAccess.class);
        SearchService searchService = new SearchServiceElastic(searchEngine, Searchclient);
        String queryResults = searchService.getIndex();
        assertTrue(queryResults == "\"tagline\": \"You Know, for Search\"");
    }

    @Test
    void givenQueryWithResults_whenputIndex_thenReturnString() throws Exception {
        String index = "samples";
        SearchEngine searchEngine = mock(SearchEngine.class);
        given(searchEngine.putIndex(index)).willReturn("{\"acknowledged\": \"true\"}");
        SearchDataAccess Searchclient = mock(SearchDataAccess.class);
        SearchService searchService = new SearchServiceElastic(searchEngine, Searchclient);
        String queryResults = searchService.putIndex(index);
        assertTrue(queryResults == "{\"acknowledged\": \"true\"}");
    }

    @Test
    void givenQueryWithResults_whenputIndexwithBody_thenReturnString() throws Exception {
        String index = "samples";
        String body = "{\"colors\":\"grey\"}";
        SearchEngine searchEngine = mock(SearchEngine.class);
        given(searchEngine.putIndex(index,body)).willReturn("{\"shards_acknowledged\": \"true\"}");
        SearchDataAccess Searchclient = mock(SearchDataAccess.class);
        SearchService searchService = new SearchServiceElastic(searchEngine, Searchclient);
        String queryResults = searchService.putIndex(index,body);
        assertTrue(queryResults == "{\"shards_acknowledged\": \"true\"}");
    }
   
   /*  @Test
    void givenQueryWithResults_whenpostDocuments_thenReturnString() throws Exception {
        String index = "samples";
        String body = "{\"peli\":\"Ant-man\"}";
        SearchEngine searchEngine = mock(SearchEngine.class);
        given(searchEngine.postDocuments(index,body)).willReturn("{\"result\": \"created\"}");
        SearchService searchService = new SearchServiceElastic(searchEngine);
        String queryResults = searchService.postDocuments(index,body);
        assertTrue(queryResults == "{\"result\": \"created\"}");
    }*/
    
    @Test
    void givenQueryWithResults_whenpostDocumentswithId_thenReturnString() throws Exception {
        String index = "samples";
        String body = "{\"peli\":\"Ant-man\"}";
        String id = "14";
        SearchEngine searchEngine = mock(SearchEngine.class);
        given(searchEngine.postDocuments(index,body,id)).willReturn("{\"result\": \"created\"}");
        SearchDataAccess Searchclient = mock(SearchDataAccess.class);
        SearchService searchService = new SearchServiceElastic(searchEngine,Searchclient);
        String queryResults = searchService.postDocuments(index,body,id);
        assertTrue(queryResults == "{\"result\": \"created\"}");
    }

    @Test
    void givenNoQuery_whenSearch_thenPropagateError() throws Exception {
        SearchEngine searchEngine = mock(SearchEngine.class);
        Throwable expectedException = new Exception("Error while searching");
        given(searchEngine.search(null)).willThrow(expectedException);
        SearchDataAccess Searchclient = mock(SearchDataAccess.class);

        SearchService searchService = new SearchServiceElastic(searchEngine,Searchclient);

        assertThrows(expectedException.getClass(), () -> searchService.search(null));
    }
}

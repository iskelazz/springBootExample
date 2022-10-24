package co.empathy.academy.demo.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

    @Test
    void givenQueryWithResults_whenSearch_thenReturnString() throws Exception {
        String query = "query with results";
        SearchEngine searchEngine = mock(SearchEngine.class);
        given(searchEngine.search(query)).willReturn("Jane");

        SearchService searchService = new SearchServiceElastic(searchEngine);

        String queryResults = searchService.search(query);

        assertTrue(queryResults == "Jane");
    }

    @Test
    void givenQueryWithResults_whenisUp_thenReturnString() throws Exception {
        SearchEngine searchEngine = mock(SearchEngine.class);
        given(searchEngine.isUp()).willReturn("You Know, for Search");

        SearchService searchService = new SearchServiceElastic(searchEngine);

        String queryResults = searchService.isUp();

        assertTrue(queryResults == "You Know, for Search");
    }


    @Test
    void givenNoQuery_whenSearch_thenPropagateError() throws Exception {
        SearchEngine searchEngine = mock(SearchEngine.class);
        Throwable expectedException = new Exception("Error while searching");
        given(searchEngine.search(null)).willThrow(expectedException);

        SearchService searchService = new SearchServiceElastic(searchEngine);

        assertThrows(expectedException.getClass(), () -> searchService.search(null));
    }
}

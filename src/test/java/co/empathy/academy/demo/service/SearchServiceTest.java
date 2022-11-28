package co.empathy.academy.demo.service;

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
    void givenQueryWithResults_whengetVersion_thenReturnString() throws Exception {
        SearchEngine searchEngine = mock(SearchEngine.class);
        given(searchEngine.getVersion()).willReturn("You Know, for Search");
        SearchDataAccess Searchclient = mock(SearchDataAccess.class);

        SearchService searchService = new SearchServiceElastic(searchEngine,Searchclient);

        String queryResults = searchService.getVersion();

        assertTrue(queryResults == "You Know, for Search");
    }

}

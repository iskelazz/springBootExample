/*package co.empathy.academy.demo.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.IOException;

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

    @Test
    void givenQueryAndField_whenTermQuery_thenMoviesReturned() throws IOException {
        String query = "query";
        String field = "field1";
        given(elasticEngine.performQuery(any(), any(), any())).willReturn(new ArrayList<Movie>() {{
            add(movie);
        }});

        List<Movie> movies = searchService.termQuery(query, field);

        assertEquals(1, movies.size());
        assertEquals(movie, movies.get(0));

        verify(queriesService, times(1)).termQuery(query, field);
        verify(elasticEngine, times(1)).performQuery(any(), any(), any());
    }
}*/

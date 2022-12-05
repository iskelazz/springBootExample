package co.empathy.academy.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.empathy.academy.demo.DAOs.SearchDataAccess;
import co.empathy.academy.demo.Models.Movie;
import co.empathy.academy.demo.supportItems.CreateMovie;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {


    @Test
    void givenQueryWithResults_whengetVersion_thenReturnString() throws Exception {
        SearchEngine searchEngine = mock(SearchEngine.class);
        given(searchEngine.getVersion()).willReturn("You Know, for Search");
        SearchDataAccess searchClient = mock(SearchDataAccess.class);

        SearchService searchService = new SearchServiceElastic(searchEngine,searchClient);

        String queryResults = searchService.getVersion();

        assertTrue(queryResults == "You Know, for Search");
    }

    @Test
    void whenqueryTermSearch_thenMoviesReturned() throws ElasticsearchException, IOException {
        String query = "query";
        String field = "Comedy";
        Movie movie = CreateMovie.getGenericMovie("1", "Generic1");
        SearchDataAccess searchDA = mock(SearchDataAccess.class);
        given(searchDA.throwQuery(any(), any(), anyInt())).willReturn(new LinkedList<Movie>() {{
            add(movie);
        }});
        SearchEngine searchEngine = mock(SearchEngine.class);
        SearchService searchService = new SearchServiceElastic(searchEngine,searchDA);

        List<Movie> movies = searchService.queryTermSearch(query, field,"simba");

        assertEquals(1, movies.size());
        assertEquals(movie, movies.get(0));

        //verify(queriesService, times(1)).termQuery(query, field);
        //verify(elasticEngine, times(1)).performQuery(any(), any(), any());
    }

    @Test
    void whenqueryTermsSearch_thenMoviesReturned() throws ElasticsearchException, IOException {
        String[] query = {"query1","query2"};
        String field = "Comedy";
        Movie movie = CreateMovie.getGenericMovie("1", "Generic1");
        SearchDataAccess searchDA = mock(SearchDataAccess.class);
        given(searchDA.throwQuery(any(), any(), anyInt())).willReturn(new LinkedList<Movie>() {{
            add(movie);
        }});
        SearchEngine searchEngine = mock(SearchEngine.class);
        SearchService searchService = new SearchServiceElastic(searchEngine,searchDA);

        List<Movie> movies = searchService.queryTermsSearch(query, field,"simba");

        assertEquals(1, movies.size());
        assertEquals(movie, movies.get(0));

    }

    @Test
    void whenmultiMatchSearch_thenMoviesReturned() throws ElasticsearchException, IOException {
        String query = "query1";
        String field = "Comedy, Thriller";
        Movie movie = CreateMovie.getGenericMovie("1", "Generic1");
        Movie movie2 = CreateMovie.getGenericMovie("2", "Generic2");
        SearchDataAccess searchDA = mock(SearchDataAccess.class);
        given(searchDA.throwQuery(any(), any(), anyInt())).willReturn(new LinkedList<Movie>() {{
            add(movie);
            add(movie2);
        }});
        SearchEngine searchEngine = mock(SearchEngine.class);
        SearchService searchService = new SearchServiceElastic(searchEngine,searchDA);

        List<Movie> movies = searchService.multiMatchSearch(query, field,"simba");

        assertEquals(2, movies.size());
        assertEquals(movie2, movies.get(1));

    }

    @Test
    void whenSearch_thenMoviesReturned() throws Exception {
        String query = "query1";
        Movie movie = CreateMovie.getGenericMovie("1", "Generic1");
        SearchDataAccess searchDA = mock(SearchDataAccess.class);
        given(searchDA.throwQuery(any(), any(), anyInt())).willReturn(new LinkedList<Movie>() {{
            add(movie);
        }});
        SearchEngine searchEngine = mock(SearchEngine.class);
        SearchService searchService = new SearchServiceElastic(searchEngine,searchDA);

        List<Movie> movies = searchService.search(query, "simba");

        assertEquals(1, movies.size());
        assertEquals(movie, movies.get(0));

    }

    @Test
    void whenProcessParam_thenMoviesReturned() throws Exception {
        //String query = "query1";
        String [] genre = {"Comedy","Sci-Fi"};
        String sortRating = "asc";
        Integer minMinutes = 100;
        String type = "movie";
        Integer nhits = 30;
        Movie movie = CreateMovie.getGenericMovie("1", "Generic1");
        SearchDataAccess searchDA = mock(SearchDataAccess.class);
        given(searchDA.throwOrderByQuery(any(), any(), any(), any())).willReturn(new LinkedList<Movie>() {{
            add(movie);
        }});
        SearchEngine searchEngine = mock(SearchEngine.class);
        SearchService searchService = new SearchServiceElastic(searchEngine,searchDA);

        List<Movie> movies = searchService.processParam("simba",genre,null,null,
            sortRating,minMinutes,null,null,null, type,nhits);

        assertEquals(1, movies.size());
        assertEquals(movie, movies.get(0));

    }
}

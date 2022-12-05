package co.empathy.academy.demo.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import co.empathy.academy.demo.Models.Movie;
import co.empathy.academy.demo.Models.hits;
import co.empathy.academy.demo.service.SearchService;
import co.empathy.academy.demo.supportItems.CreateMovie;

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

    @Test
    void whenSearch_thenResponse() throws Exception {
        String query = "query1";
        Movie movie = CreateMovie.getGenericMovie("1", "Generic1");
        hits hits = new hits(new LinkedList<Movie>() {{
            add(movie);
        }});
        SearchService service = mock(SearchService.class);

        given(service.search(any(),any())).willReturn(new LinkedList<Movie>() {{
            add(movie);
        }});

        SearchController controller = new SearchController(service);

        ResponseEntity<hits> result = controller.search(query);

        assertEquals(hits, result.getBody());
    }
    
}
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
    void givenNoQuery_whenSearch_thenPropagateError() throws Exception {
        SearchService searchService = mock(SearchService.class);
        Throwable expectedException = new Exception("Error while searching");
        given(searchService.search(null)).willThrow(expectedException);

        SearchController searchController = new SearchController(searchService);

        assertThrows(expectedException.getClass(), () -> searchController.search(null));
    }
}
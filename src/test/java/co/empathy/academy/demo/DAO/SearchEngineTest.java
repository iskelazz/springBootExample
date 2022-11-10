package co.empathy.academy.demo.DAO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;

import org.apache.http.entity.BasicHttpEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import co.empathy.academy.demo.service.SearchEngineElastic;

@ExtendWith(MockitoExtension.class)
class SearchEngineTest {

    @Test
    void givenQueryWithResults_whengetVersion_thenReturnString() throws Exception {
        String resp = "Respuesta de searchEngine";
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(resp.getBytes()));

        Response response = mock(Response.class);
        given(response.getEntity()).willReturn(entity);

        RestClient restClient = mock(RestClient.class);
        given(restClient.performRequest(any())).willReturn(response);

        SearchEngineElastic engine = new SearchEngineElastic(restClient);
        String realResp = engine.getVersion();
        assertEquals(resp,realResp);
    }

    @Test
    void givenQueryWithResults_whensearch_thenReturnString() throws Exception {
        String query = "Jane";
        String resp = "Respuesta de searchEngine";
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(resp.getBytes()));

        Response response = mock(Response.class);
        given(response.getEntity()).willReturn(entity);

        RestClient restClient = mock(RestClient.class);
        given(restClient.performRequest(any())).willReturn(response);

        SearchEngineElastic engine = new SearchEngineElastic(restClient);
        String realResp = engine.search(query);
        assertEquals(resp,realResp);
    }

/* 
    @Test
    void givenNoQuery_whenSearch_thenPropagateError() throws Exception {
        SearchEngine searchEngine = mock(SearchEngine.class);
        Throwable expectedException = new Exception("Error while searching");
        given(searchEngine.search(null)).willThrow(expectedException);

        SearchService searchService = new SearchServiceElastic(searchEngine);

        assertThrows(expectedException.getClass(), () -> searchService.search(null));
    }*/
}

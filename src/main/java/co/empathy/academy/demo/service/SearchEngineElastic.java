package co.empathy.academy.demo.service;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;

public class SearchEngineElastic implements SearchEngine{
    @Autowired
    private RestClient client;
    

    public SearchEngineElastic(RestClient client) {
        this.client = client;
    }

    //performs search with the elasticsearch library (without body)
    @Override
    public String search(String query) throws Exception {
        if (query == null) {
            throw new RuntimeException("Query is mandatory");
        }
        Request request = new Request("GET", "/_search");
        request.addParameter("q", query);
        request.addParameter("pretty", "true");
        try {
            Response response = client.performRequest(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            return responseString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //method that checks the status of the database
    @Override
    public String getVersion() throws Exception{
        Request request = new Request("GET", "/");
        request.addParameter("pretty", "true");
        try {
            Response response = client.performRequest(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            return responseString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //get indexes
    @Override
    public String getIndex() throws Exception {
        Request request = new Request("GET", "/_cat/indices");
        try {
            Response response = client.performRequest(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            return responseString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }  
    
}

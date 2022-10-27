package co.empathy.academy.demo.DAOs;
import org.json.*;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.index.mapper.SourceToParse;

public class SearchEngineElastic implements SearchEngine{
    private RestClient client;

    public SearchEngineElastic(RestClient client) {
        this.client = client;
    }

    //performs search with the elasticsearch library
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

    @Override
    public String search(String index, String Body) throws Exception {
        Request request = new Request("GET", "/" + index + "/_search");
        request.addParameter("pretty", "true");
        request.setJsonEntity(Body);
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
        //request.addParameter("pretty", "true");
        try {
            Response response = client.performRequest(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            return responseString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String putIndex(String index) throws Exception {
        Request request = new Request("PUT", "/" + index);
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

    @Override
    public String putIndex(String index, String Body) throws Exception {
        Request request = new Request("PUT", "/" + index);
        request.addParameter("pretty", "true");
        request.setJsonEntity(Body);
        try {
            Response response = client.performRequest(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            return responseString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String postDocuments(String index, String Body) throws Exception {
        Request request = new Request("POST", "/" + index + "/_doc");
        request.addParameter("pretty", "true");
        request.setJsonEntity(Body);
        try {
            Response response = client.performRequest(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            return responseString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String postDocuments(String index, String id, String Body) throws IOException {
        Request request = new Request("POST", "/" + index + "/_doc/" + id);
        request.addParameter("pretty", "true");
        request.setJsonEntity(Body);
        try {
            Response response = client.performRequest(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            return responseString;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}

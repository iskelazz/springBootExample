package co.empathy.academy.demo.service;

public interface SearchService {

    String search(String query) throws Exception;
    String search(String index, String body) throws Exception;
    String getVersion() throws Exception;
    String getIndex() throws Exception;
    String putIndex(String index) throws Exception;
    String putIndex(String index, String body) throws Exception;
    String postDocuments(String index, String body) throws Exception;
    String postDocuments(String index, String id, String body) throws Exception;


}

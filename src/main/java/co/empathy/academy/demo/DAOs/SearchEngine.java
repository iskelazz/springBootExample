package co.empathy.academy.demo.DAOs;

public interface SearchEngine {

    String search(String query) throws Exception;
    String search(String index, String Body) throws Exception;
    String getVersion() throws Exception;
    String getIndex() throws Exception;
    String putIndex(String index) throws Exception;
    String putIndex(String index, String Body) throws Exception;
    String postDocuments (String index, String Body) throws Exception;
    String postDocuments (String index, String id, String Body) throws Exception;
}

package co.empathy.academy.demo.service;


public interface SearchEngine {

    String search(String query) throws Exception;
    String getVersion() throws Exception;
    String getIndex() throws Exception;
}

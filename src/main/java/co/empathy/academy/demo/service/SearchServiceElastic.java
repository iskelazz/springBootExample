package co.empathy.academy.demo.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;

import co.empathy.academy.demo.Models.Movie;
import co.empathy.academy.demo.util.InputValidationException;
import co.empathy.academy.demo.util.ReaderTSV;
import co.empathy.academy.demo.util.Validator;

// This is the service class that will implement your search service logic
// It has a SearchEngine as a dependency
// Endpoint: /search (controller) -> SearchService -> SearchEngine

public class SearchServiceElastic implements SearchService {

    @Autowired
    private SearchEngine elasticEngine;

    public SearchServiceElastic(SearchEngine elasticEngine){
        this.elasticEngine = elasticEngine;
    }
    @Override
    public String search(String query) throws Exception {
        return elasticEngine.search(query);
    }
    @Override
    public String search(String index, String body) throws Exception {
        return elasticEngine.search(index, body);
    }
    @Override
    public String getVersion() throws Exception {
        return elasticEngine.getVersion();
    }
    @Override
    public String getIndex() throws Exception {
        return elasticEngine.getIndex();
    }
    @Override
    public String putIndex(String index) throws Exception {
        return elasticEngine.putIndex(index);
    }
    @Override
    public String putIndex(String index, String body) throws Exception {
        return elasticEngine.putIndex(index, body);
    }
    @Override
    public String postDocuments(String index, Movie body) throws Exception {
        validateMovie(body);
        return elasticEngine.addDocument(index,body);
    }
    @Override
    public String postDocuments(String index, String id, String body) throws Exception {
        return elasticEngine.postDocuments(index, id, body);
    }

    @Override
    public void mapping (String index, String mapping) throws Exception{
        elasticEngine.mapping(index, mapping);
    }

    @Override
    public void indexDatabase() throws Exception{
        File f = new File("/Users/alejandrorg/title.basics.tsv");
        ReaderTSV reader = new ReaderTSV(f);
        reader.tsvtoMovie();
    }

    public void validateMovie(Movie movie) throws InputValidationException{
        Validator.validateMandatoryString("title type", movie.getTitleType());
        
            //Validate all fields. In production
    }
    
    public String getStringFromFile(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream in = classLoader.getResourceAsStream(fileName);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }



}

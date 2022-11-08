package co.empathy.academy.demo.controller;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import co.empathy.academy.demo.util.JsonConversor;
import co.empathy.academy.demo.Models.Movie;
import co.empathy.academy.demo.service.SearchService;

@RestController
public class SearchController {
    
    @Autowired
    private SearchService searchservice;

    public SearchController(SearchService searchService){
        this.searchservice = searchService;
    }
    //Performs search in elasticSearch database in form "localhost:8080/search?query=example"
    @GetMapping("/search")
    public String search(@RequestParam(name="query") String query) throws Exception{
		  return searchservice.search(query);
    }

     //Performs search in elasticSearch database in form "localhost:8080/search?query=example"
     @GetMapping("/{index}/search")
     public String search(@PathVariable String index, @RequestBody String body) throws Exception{
           return searchservice.search(index, body);
     }

    //Get status elasticSearch database in form "localhost:8080"
    @GetMapping("")
    public String getVersion() throws Exception{
		return searchservice.getVersion();
    }

    //Gets all the indexes of the database
    @GetMapping("/_cat/indices")
    public String getIndex() throws Exception{
        return searchservice.getIndex();
    }
    
    //add a new index
    @PutMapping("/{index}")
    public String putIndex(@PathVariable String index, @RequestBody(required = false) String body) throws Exception{
        if (body == null){
            return searchservice.putIndex(index);
        } else{
            return searchservice.putIndex(index, body);
        }
    }
    
    @PutMapping(value="/{index}/mapping", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSONObject> mapping(@PathVariable String index, @RequestBody String body) throws Exception{
        searchservice.mapping(index,body);
        return ResponseEntity.created(null).body(null);
    }

    @PostMapping(value = "/{index}/_doc", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSONObject> postDocuments(@PathVariable String index, @RequestBody Movie body) throws Exception{
        try {
            searchservice.postDocuments(index,body);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
           HttpStatus.INTERNAL_SERVER_ERROR, "Server not found", e);
        }
        return ResponseEntity.created(null).body(JsonConversor.movietoJSON(body));
    }
    
    //add documents in the {index} with {id}
    @PostMapping("/{index}/_doc/{id}")
    public String postDocuments(@PathVariable String index,@PathVariable String id, @RequestBody String body) throws Exception{
        return searchservice.postDocuments(index,id, body);
    }


    @PostMapping("/database")
    public ResponseEntity<String> indexImdbData() throws Exception {
        searchservice.indexDatabase();
        return ResponseEntity.accepted().build();
    }
}

package co.empathy.academy.demo.controller;


import java.io.IOException;
import java.util.List;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import co.empathy.academy.demo.util.JsonConversor;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.empathy.academy.demo.Models.Movie;
import co.empathy.academy.demo.Models.hits;
import co.empathy.academy.demo.service.SearchService;

@RestController
public class SearchController {
    
    private static final String INDEX = "simba";
    @Autowired
    private SearchService searchservice;

    public SearchController(SearchService searchService){
        this.searchservice = searchService;
    }
    //Performs search in elasticSearch database in form "localhost:8080/search?query=example"
    @GetMapping("/_search")
    public String search(@RequestParam(name="query") String query) throws Exception{
		  return searchservice.search(query);
    }

     @GetMapping("/search/_multi")
     public ResponseEntity<List<Movie>> multiMatchSearch(@RequestParam("query") String query, @RequestParam("fields") String fields) throws ElasticsearchException, IOException {
        return ResponseEntity.ok().body(searchservice.multiMatchSearch(query, fields,INDEX));
    }

     @GetMapping(value="/search/_term", produces="application/json")
     public ResponseEntity<List<Movie>> searchByTerm (@RequestParam(name="query") String query  ,@RequestParam(name="field") String field) throws ElasticsearchException, IOException{
        return ResponseEntity.ok().body(searchservice.queryTermSearch(query, field, INDEX));
     }

     @GetMapping("/search/_terms")
     public ResponseEntity<List<Movie>> searchByTerms (@RequestParam(name="query") String query [] ,@RequestParam(name="field") String field) throws ElasticsearchException, IOException{
        return ResponseEntity.ok().body(searchservice.queryTermsSearch(query, field, INDEX));
     }

    //Get status elasticSearch database in form "localhost:8080"
    @GetMapping("")
    public String getVersion() throws Exception{
		return searchservice.getVersion();
    }

    //Gets all the indexes of the database
    @GetMapping("/_cat/indices")
    public ResponseEntity<String> getIndex() throws Exception{
        return ResponseEntity.created(null).body(searchservice.getIndex().toString());
    }

    //add a new index
    @PutMapping("/{index}")
    public ResponseEntity<String> putIndex(@PathVariable String index, @RequestBody(required = false) String body) throws Exception{
       
            searchservice.putIndex(index);
            return ResponseEntity.created(null).body(null);
    }
    
    @PutMapping(value="/mapping", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSONObject> mapping( @RequestBody String body) throws IOException{
        try {
            searchservice.mapping(INDEX,body);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
           HttpStatus.INTERNAL_SERVER_ERROR, "Server not found", e);
        }
        return ResponseEntity.created(null).body(null);
    }

    @PutMapping(value="/analyzer", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSONObject> analyzer(@RequestBody String body) throws IOException{
        try {
            searchservice.analyzer(INDEX,body);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
           HttpStatus.INTERNAL_SERVER_ERROR, "Server not found", e);
        }
        return ResponseEntity.created(null).body(null);
    }


    @PostMapping(value = "/_doc", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSONObject> postDocuments(@RequestBody Movie body) throws Exception{
        try {
            searchservice.postDocuments(INDEX,body);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
           HttpStatus.INTERNAL_SERVER_ERROR, "Server not found", e);
        }
        return ResponseEntity.created(null).body(JsonConversor.movietoJSON(body));
    }
    
    //add documents in the {index} with {id}
    @PostMapping("/_doc/{id}")
    public String postDocuments(@PathVariable String id, @RequestBody String body) throws Exception{
        return searchservice.postDocuments(INDEX,id, body);
    }

    //Builds database with the documents specified in the service
    @PostMapping("/database")
    public ResponseEntity<String> indexImdbData() throws Exception {
        searchservice.indexDatabase();
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/max_rating")
    public ResponseEntity<List<Movie>> maxAverageRating() throws Exception{
        List<Movie>body = searchservice.maxAverageRating(INDEX);
        return ResponseEntity.created(null).body(body);
    }

    @GetMapping("/min_rating")
    public ResponseEntity<List<Movie>> minAverageRating() throws Exception{
        List<Movie>body = searchservice.minAverageRating(INDEX);
        return ResponseEntity.created(null).body(body);
    }
    


    @GetMapping("/search/")
    public ResponseEntity <hits> search(
        @Nullable @RequestParam(name="genre") String [] genre,

        @Nullable @RequestParam(name="minyear") Integer minYear,
        @Nullable @RequestParam(name="maxyear") Integer maxYear,

        @Nullable @RequestParam(name="minminutes") Integer minMinutes,
        @Nullable @RequestParam(name="maxminutes") Integer maxMinutes,

        @Nullable @RequestParam(name="minscore") Float minScore,
        @Nullable @RequestParam(name="maxscore") Float maxScore,

        @Nullable @RequestParam(name="type") String type
    ) throws Exception{
        List<Movie>body = searchservice.processParam("simba",genre,minYear,maxYear,minMinutes,maxMinutes
            ,minScore,maxScore,type);
        hits hits = new hits(body);
        return ResponseEntity.created(null).body(hits);
    }

}

package co.empathy.academy.demo.controller;


import java.io.IOException;
import java.util.List;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    @Operation(summary = "This operation search by match with originalTitle of the content of imdb")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The search process has been successfully completed."),
        @ApiResponse(responseCode = "500", description = "Error in the search process")
    })
    @GetMapping(value = "/_search", produces = "application/json")
    public ResponseEntity <hits> search(@RequestParam(name="query") String query) throws Exception{
	try{	
        List<Movie>list_movies = searchservice.search(query,INDEX); 
        hits hits = new hits(list_movies);
        return ResponseEntity.ok(hits);
    } catch (Exception e){
        return ResponseEntity.internalServerError().build();   
    }    
    }

    //performs a search by filtering by various parameters
    @Operation(summary = "This operation search several terms for a several fields")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The search process has been successfully completed."),
        @ApiResponse(responseCode = "500", description = "Error in the search indices")
    })
     @GetMapping(value = "/search/_multi",produces = "application/json")
     public ResponseEntity<List<Movie>> multiMatchSearch(@RequestParam("query") String query, @RequestParam("fields") String fields) throws ElasticsearchException, IOException {
        try{
            return ResponseEntity.ok(searchservice.multiMatchSearch(query, fields,INDEX));
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();   
        }    
    }

    //performs a search by filtering by a parameter and a field
    @Operation(summary = "This operation search a single term for a single field")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The search process has been successfully completed."),
        @ApiResponse(responseCode = "500", description = "Error in the search process")
    })
     @GetMapping(value="/search/_term", produces="application/json")
     public ResponseEntity<List<Movie>> searchByTerm (@RequestParam(name="query") String query  ,@RequestParam(name="field") String field) throws ElasticsearchException, IOException{
        try{
            return ResponseEntity.ok(searchservice.queryTermSearch(query, field, INDEX));
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();

        }    
     }

     //performs a search by filtering by several paremeters and a field
     @Operation(summary = "This operation search several terms for a single field")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The search process has been successfully completed."),
        @ApiResponse(responseCode = "500", description = "Error in the search process")
    })
     @GetMapping(value="/search/_terms", produces = "application/json")
     public ResponseEntity<List<Movie>> searchByTerms (@RequestParam(name="query") String query [] ,@RequestParam(name="field") String field) throws ElasticsearchException, IOException{
        try{
            return ResponseEntity.ok(searchservice.queryTermsSearch(query, field, INDEX));
        } catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
     }

    //Get status elasticSearch database in form "localhost:8080"
    @GetMapping("")
    public String getVersion() throws Exception{
		return searchservice.getVersion();
    }

    //Gets all the indexes of the database
    @Operation(summary = "Get the indices")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The indices has been returned."),
        @ApiResponse(responseCode = "500", description = "Error in the search indices")
    })
    @GetMapping(value = "/_cat/indices", produces = "application/json" )
    public ResponseEntity<String> getIndex() throws Exception{
        try{
            return ResponseEntity.ok(searchservice.getIndex().toString());
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    //add a new index
    @Operation(summary = "Put a index with a name passed by parameter")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Data accepted"),
        //@ApiResponse(responseCode = "400", description = "Error, index no accepted"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PutMapping(value = "/{index}", produces = "application/json")
    public ResponseEntity<String> putIndex(@PathVariable String index, @RequestBody(required = false) String body) throws Exception{
        try{
            searchservice.putIndex(index);
            return ResponseEntity.ok(null);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    //add a "simba"(INDEX constant) index. This is a pre-builder index for store the imdb database
    @Operation(summary = "Put a predefinided index")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Index accepted"),
        //@ApiResponse(responseCode = "400", description = "Error, index no accepted"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PutMapping(value = "/index", produces = "application/json")
    public ResponseEntity<String> putIndex() throws Exception{
        try{
            searchservice.putIndex(INDEX);
            return ResponseEntity.ok(null);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
    
    //add analyzer using a body
    @Operation(summary = "Put a mapping by body(postman/insomnia)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mapping accepted"),
        //@ApiResponse(responseCode = "400", description = "Error, mapping no accepted"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    //add mapping using a body
    @PutMapping(value="/mapping", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSONObject> mapping( @RequestBody String body) throws IOException{
        try {
            searchservice.mapping(INDEX,body);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); 
        }
    }

    //add analyzer using a body
    @Operation(summary = "Put a analyzer by body(postman/insomnia)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Analyzer accepted"),
        //@ApiResponse(responseCode = "400", description = "Error, analyzer no accepted"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PutMapping(value="/analyzer", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JSONObject> analyzer(@RequestBody String body) throws IOException{
        try {
            searchservice.analyzer(INDEX,body);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); 
        }
    }

    //add a document 
    @Operation(summary = "Indexing a generic doc in elasticsearch database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Data accepted"),
        //@ApiResponse(responseCode = "400", description = "Error indexing"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PostMapping(value = "/_doc", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> postDocument(@RequestBody Movie body) throws Exception{
        try {
            searchservice.postDocument(INDEX,body);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    //add a document 
    @Operation(summary = "Indexing a generic doc in elasticsearch database, with id passed by parameter")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Data accepted"),
        //@ApiResponse(responseCode = "400", description = "Error indexing"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    //add documents in the {index} with {id}
    @PostMapping(value = "/_doc/{id}",consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> postDocument(@PathVariable String id, @RequestBody Movie body) throws Exception{
        try {
            searchservice.postDocument(INDEX,id,body);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Indexing imdb tsvs in elastic search")
    @Parameter(name = "file_basics",description = "file with basics")
    @Parameter(name = "file_ratings",description = "file with ratings")
    @Parameter(name = "file_akas",description = "file with akas")
    @Parameter(name = "file_crew",description = "file with crew")
    @Parameter(name = "file_principals",description = "file with starring")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Data accepted"),
        //@ApiResponse(responseCode = "400", description = "Error indexing"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    //Builds database with the documents specified in the service
    @PostMapping(value = "/database", produces = "application/json")
    public ResponseEntity<String> indexImdbData(
        @RequestParam(name="basics") String file_basics,
        @RequestParam(name="ratings") String file_ratings,
        @RequestParam(name="akas") String file_akas,
        @RequestParam(name="crew") String file_crew,
        @RequestParam(name="principals") String file_principals
    ) throws Exception {
        try{
            searchservice.indexDatabase(file_basics, file_ratings, file_akas,file_crew, file_principals);
            return ResponseEntity.accepted().build();
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /* Performs a database search with a series of filters and aggregations passed by parameters,
     * by default, return top_hits of Numvotes, but if use sortRating key return top_hits of averageRating
     * with a minimun of 15000 votes for filter relevance of rating
     */
    @Operation(summary = "Get content of imdb database")
    @Parameter(name = "genres", description = "Genres of content, can be several")
    @Parameter(name = "type", description = "Type of content. Only one")
    @Parameter(name = "minYear", description = "Return content released that year(min year) and the following years")
    @Parameter(name = "maxYear", description = "Return content released that year(max year) and the previous years")
    @Parameter(name = "minMinutes", description = "Filter results to return it above a minimun duration")
    @Parameter(name = "maxMinutes", description = "Filter results to return it below a maximun duration")
    @Parameter(name = "minScore", description = "Filter results to return it above a given rating")
    @Parameter(name = "maxScore", description = "Filter results to return it below a given rating")
    @Parameter(name = "maxNHits", description = "Number of documents returned")
    @Parameter(name = "sortRating", description = "sort by rating in ascending or descending order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The search process has been successfully completed."),
            @ApiResponse(responseCode = "500", description = "Error in the search process")
    })
    @GetMapping(value ={"/search/","/search"}, produces = "application/json")
    public ResponseEntity <hits> search(
        @Nullable @RequestParam(name="genres") String [] genres,

        @Nullable @RequestParam(name="minYear") Integer minYear,
        @Nullable @RequestParam(name="maxYear") Integer maxYear,

        @Nullable @RequestParam(name="minMinutes") Integer minMinutes,
        @Nullable @RequestParam(name="maxMinutes") Integer maxMinutes,

        @Nullable @RequestParam(name="minScore") Float minScore,
        @Nullable @RequestParam(name="maxScore") Float maxScore,

        @Nullable @RequestParam(name="type") String type,
        @Nullable @RequestParam(name="maxNHits") Integer nhits,
        @Nullable @RequestParam(name="sortRating") String sortRating
    ) throws Exception{
        try{
            List<Movie>body = searchservice.processParam("simba",genres,minYear,maxYear,sortRating
            ,minMinutes,maxMinutes,minScore,maxScore,type,nhits);
            hits hits = new hits(body);
            return ResponseEntity.ok(hits);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

}

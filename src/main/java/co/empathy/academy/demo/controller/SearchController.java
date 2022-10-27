package co.empathy.academy.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.empathy.academy.demo.service.SearchService;

@RestController
public class SearchController {
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
    //add documents in the {index} with id auto-generated
    @PostMapping("/{index}/_doc")
    public String postDocuments(@PathVariable String index, @RequestBody String body) throws Exception{
        return searchservice.postDocuments(index, body);
    }
    
    //add documents in the {index} with {id}
    @PostMapping("/{index}/_doc/{id}")
    public String postDocuments(@PathVariable String index,@PathVariable String id, @RequestBody String body) throws Exception{
        return searchservice.postDocuments(index,id, body);
    }

}

package co.empathy.academy.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
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
    //Get status elasticSearch database in form "localhost:8080"
    @GetMapping("")
    public String isUp() throws Exception{
		  return searchservice.isUp();
    }
}

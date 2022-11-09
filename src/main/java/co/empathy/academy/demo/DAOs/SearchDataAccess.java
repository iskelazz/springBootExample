package co.empathy.academy.demo.DAOs;

import java.util.List;

import co.empathy.academy.demo.Models.Movie;

public interface SearchDataAccess {
    void bulk (List<Movie> movies, String index) throws Exception;
}

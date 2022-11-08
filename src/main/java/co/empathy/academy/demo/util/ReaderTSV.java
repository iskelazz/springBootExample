package co.empathy.academy.demo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import co.empathy.academy.demo.Models.Movie;

public class ReaderTSV {
    private Integer numberRow = 0;
    //private boolean isfinished = false;
    private final BufferedReader reader;

    public ReaderTSV(File f) {
        try {
            this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Movie> tsvtoMovie() throws IOException{
        
        String Headers = reader.readLine();
        System.out.println(Headers);
        //Integer finishBulk = numberRow + size;
        List<Movie> results = new LinkedList<>();
        while (numberRow<10){
            String line = null;
            line = reader.readLine();
            if (line == null) {
                //isfinished = true;
                break;
            } 
            String[] movie_tsv = line.split("\t");
            Movie movie = new Movie(movie_tsv[0], movie_tsv[1], movie_tsv[2], movie_tsv[3], false, movie_tsv[5], 
            movie_tsv[6],Integer.parseInt(movie_tsv[7]), movie_tsv[8]);
            System.out.println(movie.toString());
            results.add(movie);
            numberRow++;
            }
            return results;
    }
}

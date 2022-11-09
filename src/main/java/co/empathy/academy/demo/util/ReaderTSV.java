package co.empathy.academy.demo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import co.empathy.academy.demo.Models.Movie;

public class ReaderTSV {
    private Integer count = 0;
    private boolean isfinished = false;
    private final BufferedReader reader;

    public ReaderTSV(File f) {
        try {
            this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String extractLine() throws IOException{
        String Headers = reader.readLine();
        return Headers;
    }

    public LinkedList<Movie> tsvtoMovies() throws IOException{
        Integer numberRow = 0;

        LinkedList<Movie> results = new LinkedList<>();
        while (numberRow<10000){
            String line = null;
            line = reader.readLine();
            if (line == null) {
                isfinished = true;
                break;
            } 
            String[] movie_tsv = line.split("\t");
            if (movie_tsv[4].equals("0")){
                Movie movie = new Movie(movie_tsv[0], movie_tsv[1], movie_tsv[2], movie_tsv[3], false, movie_tsv[5], 
                movie_tsv[6], toInt(movie_tsv[7]), movie_tsv[8]);
                results.add(movie);
                numberRow++;
            }
        }
        count=count+numberRow;
        System.out.println(count);
        return results;
    }

    public int getCount(){
        return count;
    }

    public boolean getFinished(){
        return isfinished;
    }

    public static int toInt(String value) {
        if (value.trim().contentEquals("\\N")) {
            return 0;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

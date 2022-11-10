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
    private int count = 0;
    private boolean isfinished = false;
    private final BufferedReader reader_basics;
    private final BufferedReader reader_ratings;
    boolean progress_ratings = true;
    boolean progress_basics = true;
    

    public ReaderTSV(File basics, File ratings) {
        try {
            this.reader_basics = new BufferedReader(new InputStreamReader(new FileInputStream(basics)));
            this.reader_ratings = new BufferedReader(new InputStreamReader(new FileInputStream(ratings)));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String extractHeadersBasics() throws IOException{
        String Headers = reader_basics.readLine();
        return Headers;
    }

    public String extractHeadersRatings() throws IOException{
        String Headers = reader_ratings.readLine();
        return Headers;
    }

    public LinkedList<Movie> tsvtoMovies() throws IOException{
        Integer numberRow = 0;
        String line_ratings=null;
        String line_basics = null;
        
        LinkedList<Movie> results = new LinkedList<>();
        while (numberRow<20000 || progress_ratings == false || progress_basics == false){
            String rating = "";
            String numVotes = "";
            if (progress_basics){
                line_basics = null;
                line_basics = reader_basics.readLine();
            }
            if (progress_ratings){
                line_ratings = null;
                line_ratings = reader_ratings.readLine();
            }
            if (line_basics == null || line_ratings == null) {
                isfinished = true;
                break;
            } 
            String[] basics_tsv = line_basics.split("\t");
            String[] ratings_tsv = line_ratings.split("\t");
            System.out.println(numberRow + ",progress basics: " + progress_basics + ",progress ratings: "+ progress_ratings);
            compareID(basics_tsv[0], ratings_tsv[0]);
            System.out.println(basics_tsv[0] + " ratings: " + ratings_tsv[0]);
            if(progress_basics){
                if (basics_tsv[0].equals(ratings_tsv[0])){
                    rating = ratings_tsv[1];
                    numVotes = ratings_tsv[2];
                }
                else{
                    rating = "0";
                    numVotes = "0";
                }
                if (basics_tsv[4].equals("0")){
                    Movie movie = new Movie(basics_tsv[0], basics_tsv[1], basics_tsv[2], basics_tsv[3], false, basics_tsv[5], 
                    basics_tsv[6], toInt(basics_tsv[7]), basics_tsv[8],rating,numVotes);
                    System.out.println(movie.toString());
                    results.add(movie);
                    numberRow++;
                }
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


    public boolean compareID(String id_basics, String id_ratings){
        int basics = toInt(id_basics.substring(2,9));
        int ratings = toInt(id_ratings.substring(2,9));
        System.out.println("Basics " + basics + ", ratings: " + ratings);
        
        if (basics == ratings){
            if (id_basics.length()==id_ratings.length()){
                if (id_basics.length()==10){
                    basics = toInt(id_basics.substring(2, 10));
                    ratings = toInt(id_ratings.substring(2, 10));
                    if (basics == ratings){
                        progress_basics = true;
                        progress_ratings = true;
                        return true;
                    }
                    if (basics > ratings){
                        progress_basics = false;
                        progress_ratings = true;
                        return false;
                    } else {
                        progress_basics = true;
                        progress_ratings = false;
                        return false;
                    }
                }
                progress_basics = true;
                progress_ratings = true;
                return true;
            }
            if (id_basics.length()>id_ratings.length()) {
                progress_basics = true;
                progress_ratings = false;
                return false;
                 //Se avanza solo en basics

            } else {
                progress_basics = false;
                progress_ratings = true;
                return false;
                //Se avanza solo en ratings
            }  
        }
        if (basics > ratings){
            progress_basics = false;
            progress_ratings = true;
            return false;
            //Se avanza solo en ratings
        } else {
            progress_basics = true;
            progress_ratings = false;
            return false;
            // Se avanza solo en basics
        }
    }

    public static int toInt(String value) {
        if (value.trim().contentEquals("\\N")) {
            //System.out.println(value);
            return 0;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
           // System.out.println(value);
            return 0;
        }
    }
}

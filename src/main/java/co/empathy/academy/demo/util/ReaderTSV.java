package co.empathy.academy.demo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import co.empathy.academy.demo.Models.Movie;

public class ReaderTSV {
    private int count = 0;
    private boolean isfinished;
    private final BufferedReader reader_basics;
    private final BufferedReader reader_ratings;
    private boolean progress_ratings;
    private boolean progress_basics;
    private LinkedList<String[]> ratingslist = new LinkedList<>();

    

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

    public LinkedList<Movie> tsvToMovies() throws IOException{
        Integer numberRow = 0;
        String line_ratings=null;
        String line_basics = null;
        progress_basics = true;
        progress_ratings = true;
        isfinished = false;
        LinkedList<Movie> results = new LinkedList<>();
        while (numberRow<20000 || progress_basics == false || progress_ratings == false){
            String rating = null;
            String numVotes = null;
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
            line_ratings = findIDMatch(basics_tsv[0], line_ratings, numberRow);
            if (progress_basics && progress_ratings){
                String [] last_rating = line_ratings.split("\t");
                if (last_rating[0].equals(basics_tsv[0])){
                    rating = last_rating[1];
                    numVotes = last_rating[2]; 
                } 
                if (rating == null){
                    rating = "0";
                    numVotes = "0";
                }
                if (basics_tsv[4].equals("0")){
                    List<String> genre = Arrays.asList(basics_tsv[8].split(","));
                    Movie movie = new Movie(basics_tsv[0], basics_tsv[1], basics_tsv[2], basics_tsv[3], false, toInt(basics_tsv[5]), 
                    basics_tsv[6], toInt(basics_tsv[7]), genre,toDouble(rating),toInt(numVotes));
                    //System.out.println(movie.toString());
                    results.add(movie);
                    numberRow++;
                }
            } else if (progress_basics){
                for (String [] rat : ratingslist) { 
                    if (rat[0].equals(basics_tsv[0])){
                        rating = rat[1];
                        numVotes = rat[2];
                        //ratingslist.remove(rat); 
                        break;
                    }
                }
                if (rating == null){
                    rating = "0";
                    numVotes = "0";
                }
                if (basics_tsv[4].equals("0")){
                    List<String> genre = Arrays.asList(basics_tsv[8].split(","));
                    Movie movie = new Movie(basics_tsv[0], basics_tsv[1], basics_tsv[2], basics_tsv[3], false, toInt(basics_tsv[5]), 
                    basics_tsv[6], toInt(basics_tsv[7]), genre,toDouble(rating),toInt(numVotes));
                    //System.out.println(movie.toString());
                    results.add(movie);
                    numberRow++;
                }
            }
        }
        count = count + numberRow;        
        System.out.println(count);
        return results;
    }

    public int getCount(){
        return count;
    }

    public boolean getFinished(){
        return isfinished;
    }

    private String findIDMatch (String id_basics, String line_ratings, int numberRow) throws IOException{
        String [] processed_ratings = line_ratings.split("\t");
        int basics = toInt(id_basics.substring(2,9));
        int ratings = toInt(processed_ratings[0].substring(2,9));
        //System.out.println("Basics " + basics + ", ratings: " + ratings);
        String newLine = null;
        if (basics == ratings){
            ratingslist.clear();
            if (numberRow>20000) {
                progress_basics = true;
                progress_ratings = true;
                return line_ratings;
            }
            while (basics == ratings){
                ratingslist.add(processed_ratings);
                newLine = null;
                newLine = reader_ratings.readLine();
                if (newLine == null) break;
                processed_ratings = newLine.split("\t");
                ratings = toInt(processed_ratings[0].substring(2, 9));
            }
            progress_ratings = false;
            progress_basics = true;
            return newLine;
        } else if (basics > ratings) {
            progress_ratings = true;
            progress_basics = false;
            return line_ratings;
        } else {
            progress_ratings = false;
            progress_basics = true;
            return line_ratings;
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

    public static double toDouble(String value) {
        if (value.trim().contentEquals("\\N")) {
            //System.out.println(value);
            return 0;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
           // System.out.println(value);
            return 0;
        }
    }
}

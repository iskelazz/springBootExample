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

import co.empathy.academy.demo.Models.Aka;
import co.empathy.academy.demo.Models.Director;
import co.empathy.academy.demo.Models.Movie;
import co.empathy.academy.demo.Models.Name;
import co.empathy.academy.demo.Models.Starring;

public class ReaderTSV {
    private int count = 0;
    private boolean isfinished;
    private final BufferedReader reader_basics;
    private final BufferedReader reader_ratings;
    private final BufferedReader reader_akas;
    private final BufferedReader reader_crew;
    private final BufferedReader reader_principals;

    private boolean progress_ratings;
    private boolean progress_basics;
    private boolean progress_akas;
    private boolean progress_crew;
    private boolean progress_principals;

    private LinkedList<String[]> ratingslist = new LinkedList<>();
    private LinkedList<String[]> akaslist = new LinkedList<>();
    private LinkedList<String[]> crewlist = new LinkedList<>();
    private LinkedList<String[]> principalslist = new LinkedList<>();


    

    public ReaderTSV(File basics, File ratings, File akas, File crew, File principals) {
        try {
            this.reader_basics = new BufferedReader(new InputStreamReader(new FileInputStream(basics)));
            this.reader_ratings = new BufferedReader(new InputStreamReader(new FileInputStream(ratings)));
            this.reader_akas = new BufferedReader(new InputStreamReader(new FileInputStream(akas)));
            this.reader_crew = new BufferedReader(new InputStreamReader(new FileInputStream(crew)));
            this.reader_principals = new BufferedReader(new InputStreamReader(new FileInputStream(principals)));
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

    public String extractHeadersAkas() throws IOException{
        String Headers = reader_akas.readLine();
        return Headers;
    }

    public String extractHeadersCrew() throws IOException{
        String Headers = reader_crew.readLine();
        return Headers;
    }

    public String extractHeadersPrincipals() throws IOException{
        String Headers = reader_principals.readLine();
        return Headers;
    }

    public void extractHeaders() throws IOException{
        extractHeadersBasics();
        extractHeadersRatings();
        extractHeadersAkas();
        extractHeadersCrew();
        extractHeadersPrincipals();
    }

    public LinkedList<Movie> tsvToMovies() throws IOException{
        Integer numberRow = 0;

        String line_ratings=null;
        String line_basics = null;
        String line_akas = null;
        String line_crew = null;
        String line_principals = null;

        progress_basics = true;
        progress_ratings = true;
        progress_akas = true;
        progress_crew = true;
        progress_principals = true;

        isfinished = false;
        extractHeaders();
        LinkedList<Movie> results = new LinkedList<>();
        while (numberRow<20000 || progress_basics == false || progress_ratings == false 
            || progress_akas==false || progress_crew == false || progress_principals == false){
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
            if (progress_akas){
                line_akas = null;
                line_akas = reader_akas.readLine();
            }
            if (progress_crew){
                line_crew = null;
                line_crew = reader_crew.readLine();
            }
            if (progress_principals){
                line_principals = null;
                line_principals = reader_principals.readLine();
            }

            if (line_basics == null || line_ratings == null || line_akas == null || 
                line_crew == null || line_principals == null) {
                isfinished = true;
                break;
            }
            String[] basics_tsv = line_basics.split("\t");
            
            line_ratings = findIDGenerics(basics_tsv[0], line_ratings, numberRow, ratingslist, "ratings", reader_ratings);
            if (progress_basics) line_akas = findIDGenerics(basics_tsv[0], line_akas, numberRow, akaslist, "akas", reader_akas);
            else progress_akas=false;
            if (progress_basics) line_crew = findIDGenerics(basics_tsv[0], line_crew, numberRow, crewlist, "crew", reader_crew);
            else progress_crew=false;
            if (progress_basics) line_principals = findIDGenerics(basics_tsv[0], line_principals, numberRow, principalslist, "principals", reader_principals);
            else progress_principals=false;
            if (progress_basics && progress_ratings && progress_akas && progress_crew && progress_principals){
                String [] last_rating = line_ratings.split("\t");
                if (last_rating[0].equals(basics_tsv[0])){
                    rating = last_rating[1];
                    numVotes = last_rating[2]; 
                } 
                if (rating == null){
                    rating = "0";
                    numVotes = "0";
                }
                List<Aka> index_aka = new LinkedList<>();
                for (String [] ak : akaslist) { 
                    if (ak[0].equals(basics_tsv[0])){
                        Aka result = new Aka(ak[1],ak[2],ak[3],toBool(ak[4]));
                        index_aka.add(result);
                    }
                }
                List<Director> index_crew = new LinkedList<>();
                for (String [] crw : crewlist) {
                    if (crw[0].equals(basics_tsv[0])){
                        List<String>Director_string = Arrays.asList(crw[1].split(","));
                        for (int i = 0; Director_string.size()>i; i++){
                            Director director = new Director(Director_string.get(i));
                            index_crew.add(director);
                        }
                        break;
                    }
                }
                List <Starring> index_principals = new LinkedList<>();
                for (String [] princ : principalslist){
                    if(princ[0].equals(basics_tsv[0])){
                        Name name = new Name(princ[2]);
                        Starring starring = new Starring(name,princ[5]);
                        index_principals.add(starring);
                    }
                }
                if (basics_tsv[4].equals("0")){
                    List<String> genres = Arrays.asList(basics_tsv[8].split(","));
                    Movie movie = new Movie(basics_tsv[0], basics_tsv[1], basics_tsv[2], basics_tsv[3], false, toInt(basics_tsv[5]), 
                    basics_tsv[6], toInt(basics_tsv[7]), genres,toDouble(rating),toInt(numVotes),index_aka,index_crew,index_principals);
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
                List<Aka> index_aka = new LinkedList<>();
                for (String [] ak : akaslist) { 
                    if (ak[0].equals(basics_tsv[0])){
                        Aka result = new Aka(ak[1],ak[2],ak[3],toBool(ak[4]));
                        index_aka.add(result);
                        //akaslist.remove(ak); 
                    }
                }
                List<Director> index_crew = new LinkedList<>();
                for (String [] crw : crewlist) {
                    if (crw[0].equals(basics_tsv[0])){
                        List<String>Director_string = Arrays.asList(crw[1].split(","));
                        for (int i = 0; Director_string.size()>i; i++){
                            Director director = new Director(Director_string.get(i));
                            index_crew.add(director);
                        }
                        break;
                    }
                }
                List <Starring> index_principals = new LinkedList<>();
                for (String [] princ : principalslist){
                    if(princ[0].equals(basics_tsv[0])){
                        Name name = new Name(princ[2]);
                        Starring starring = new Starring(name,princ[5]);
                        index_principals.add(starring);
                    }
                }
                //System.out.println(index_aka.toString());
                if (basics_tsv[4].equals("0")){
                    List<String> genres = Arrays.asList(basics_tsv[8].split(","));
                    Movie movie = new Movie(basics_tsv[0], basics_tsv[1], basics_tsv[2], basics_tsv[3], false, toInt(basics_tsv[5]), 
                    basics_tsv[6], toInt(basics_tsv[7]), genres,toDouble(rating),toInt(numVotes),index_aka,index_crew, index_principals);
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

    private String findIDGenerics(String id_basics, String line_file, int numberRow, 
        List<String []> list, String key, BufferedReader aReader) throws IOException{
        String [] processed = line_file.split("\t");
        int basics = toInt(id_basics.substring(2,9));
        int generic = toInt(processed[0].substring(2,9));
        //System.out.println("Basics " + basics + ", ratings: " + ratings);
        String newLine = null;
        if (basics == generic){
            list.clear();
            if (numberRow>20000) {
                progress_basics = true;
                change_bool(key, true);
                return line_file;
            }
            while (basics == generic){
                list.add(processed);
                newLine = null;
                newLine = aReader.readLine();
                if (newLine == null) break;
                processed = newLine.split("\t");
                generic = toInt(processed[0].substring(2, 9));
            }
            change_bool(key, false);
            progress_basics = true;
            return newLine;
        } else if (basics > generic) {
            change_bool(key, true);
            progress_basics = false;
            return line_file;
        } else {
            change_bool(key, false);
            progress_basics = true;
            return line_file;
        }
    }


    private void change_bool(String key, Boolean value){
        if (key.equals("akas")){
            progress_akas = value;
        }
        if (key.equals("crew")){
            progress_crew = value;
        }
        if (key.equals("ratings")){
            progress_ratings = value;
        }
        if (key.equals("principals")){
            progress_principals = value;
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

    public static Boolean toBool(String value) {
            if (value.trim().contentEquals("\\N")) {
                return false;
            }
            try {
                if (value.trim().contentEquals("1")) return true;
                else return false;
            } catch (NumberFormatException e) {
               // System.out.println(value);
                return false;
            }
    }
}

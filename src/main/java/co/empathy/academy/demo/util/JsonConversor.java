package co.empathy.academy.demo.util;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import co.empathy.academy.demo.Models.Aka;
import co.empathy.academy.demo.Models.Director;
import co.empathy.academy.demo.Models.Movie;
import co.empathy.academy.demo.Models.Name;
import co.empathy.academy.demo.Models.Starring;

public class JsonConversor {
    //less the genre list. No used yet
    public static Movie jsontoMovie(JSONObject json){
        LinkedList<String> genres = new LinkedList<>();
        JSONArray subItemArray;
        JSONArray akaArray = json.optJSONArray("akas");
        JSONArray directorArray = json.optJSONArray("directors");
        JSONArray starringArray = json.optJSONArray("starring");
        subItemArray = json.optJSONArray("genres");
        if (null != subItemArray) {
            for (int j = 0; j < subItemArray.length(); j++) {
                String subItemObject = subItemArray.optString(j);
                String subItem = subItemObject;
                genres.add(subItem);
            }
        }
        return new Movie(
                json.optString("id"),
                json.optString("titleType"),
                json.optString("primaryTitle"),
                json.optString("originalTitle"),
                json.optBoolean("isAdult"),
                json.optInt("startYear"),
                json.optString("endYear"),
                json.optInt("runtimeMinutes"),
                genres,
                json.optDouble("averageRating"),
                json.optInt("numVotes"),
                jsontoAka(akaArray),
                jsontoDirector(directorArray),
                jsontoStarring(starringArray)
        );
    }



    public static List<Movie> jsontoMovies(JSONArray data){
        LinkedList<Movie> result = new LinkedList<>();
        JSONArray subItemArray;
        JSONArray akaArray;
        JSONArray directorArray;
        JSONArray starringArray;

        for (int i = 0; i < data.length(); i++) {
            JSONObject json = data.getJSONObject(i);
            subItemArray = json.optJSONArray("genres");
            akaArray = json.optJSONArray("akas");
            directorArray = json.optJSONArray("directors");
            starringArray = json.optJSONArray("starring");
            LinkedList<String> genres = new LinkedList<>();

            if (null != subItemArray) {
                for (int j = 0; j < subItemArray.length(); j++) {
                    String subItemObject = subItemArray.optString(j);
                    String subItem = subItemObject;
                    genres.add(subItem);
                }
    }
            Movie castor = new Movie (
                json.optString("id"),
                json.optString("titleType"),
                json.optString("primaryTitle"),
                json.optString("originalTitle"),
                json.optBoolean("isAdult"),
                json.optInt("startYear"),
                json.optString("endYear"),
                json.optInt("runtimeMinutes"),
                genres,
                json.optDouble("averageRating"),
                json.optInt("numVotes"),
                jsontoAka(akaArray),
                jsontoDirector(directorArray),
                jsontoStarring(starringArray)
            );
            result.add(castor);
        }
        return result;
    }

    public static List<Aka> jsontoAka(JSONArray data){
        List<Aka> result = new LinkedList<>();
        for (int i = 0; i < data.length(); i++) {
            JSONObject json = data.getJSONObject(i);
            Aka aka = new Aka (
                json.optString("title"),
                json.optString("region"),
                json.optString("language"),
                json.optBoolean("isOriginalTitle")
            );
            result.add(aka);
        }
        return result;
    }    

    public static List<Director> jsontoDirector(JSONArray data){
        List<Director> result = new LinkedList<>();
        for (int i = 0; i < data.length(); i++) {
            JSONObject json = data.getJSONObject(i);
            Director director = new Director (
                json.optString("nconst")
            );
            result.add(director);
        }
        return result;
    } 
    
    public static List<Starring> jsontoStarring (JSONArray data){
        List<Starring> result = new LinkedList<>();

        for (int i = 0; i < data.length(); i++) {
            JSONObject json = data.getJSONObject(i);
            
            Starring starring = new Starring (
                jsontoName(json.getJSONObject("name")),
                json.optString("characters")
            );
            result.add(starring);
        }
        return result;
    }

    public static Name jsontoName (JSONObject name){
        return new Name(name.optString("nconst")); 
    }

    //less the genre list. No used yet
    public static JSONObject movietoJSON (Movie movie){
        JSONObject jsonObject = new JSONObject();
        try {
            Field changeMap = jsonObject.getClass().getDeclaredField("map");
            changeMap.setAccessible(true);
            changeMap.set(jsonObject, new LinkedHashMap<>());
            changeMap.setAccessible(false);
          } catch (IllegalAccessException | NoSuchFieldException e) {
          }
        
            jsonObject.put("id", movie.getId());
            jsonObject.put("type", movie.getTitleType());
            jsonObject.put("primaryTitle", movie.getPrimaryTitle());
            jsonObject.put("originalTitle", movie.getOriginalTitle());
            jsonObject.put("isAdult", movie.getIsAdult());
            jsonObject.put("startYear", movie.getStartYear());
            jsonObject.put("endYear", movie.getEndYear());
            jsonObject.put("runtimeMinutes", movie.getRuntimeMinutes());
            jsonObject.put("genres", movie.getGenres());
            jsonObject.put("averageRating",movie.getAverageRating());
            jsonObject.put("numVotes",movie.getNumVotes());
            jsonObject.put("akas",movie.getAkas());
            jsonObject.put("directors",movie.getDirectors());
            jsonObject.put("starring",movie.getStarring());

            return jsonObject;      
    }

    public static JSONArray moviestoJSON (List<Movie> movies){
        JSONArray hits_array = new JSONArray();
        
        try {
            Field changeMap = hits_array.getClass().getDeclaredField("map");
            changeMap.setAccessible(true);
            changeMap.set(hits_array, new LinkedHashMap<>());
            changeMap.setAccessible(false);
          } catch (IllegalAccessException | NoSuchFieldException e) {
          }
        for (Movie movie : movies){
            JSONObject mov = new JSONObject();
            mov.put("hits",movie);
            hits_array.put(mov);
        }
        return hits_array;
    }
}

package co.empathy.academy.demo.util;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import co.empathy.academy.demo.Models.Movie;

public class JsonConversor {
    //less the genre list. No used yet
    public static Movie jsontoMovie(JSONObject json){
        return new Movie(
                json.optString("id"),
                json.optString("titleType"),
                json.optString("primaryTitle"),
                json.optString("originalTitle"),
                json.optBoolean("isAdult"),
                json.optString("startYear"),
                json.optString("endYear"),
                json.optInt("runtimesMinutes"),
                json.optString("genre"),
                json.optDouble("averageRating"),
                json.optInt("numVotes")
        );
    }

    public static List<Movie> jsontoMovies(JSONArray data){
        LinkedList<Movie> result = new LinkedList<>();
        for (int i = 0; i < data.length(); i++) {
            JSONObject json = data.getJSONObject(i);
            Movie castor = new Movie (
                json.optString("id"),
                json.optString("titleType"),
                json.optString("primaryTitle"),
                json.optString("originalTitle"),
                json.optBoolean("isAdult"),
                json.optString("startYear"),
                json.optString("endYear"),
                json.optInt("runtimesMinutes"),
                json.optString("genre"),
                json.optDouble("averageRating"),
                json.optInt("numVotes")
            );
            result.add(castor);
        }
        return result;
    }

    //less the genre list. No used yet
    public static JSONObject movietoJSON (Movie movie){
        return new JSONObject()
            .put("id", movie.getId())
            .put("type", movie.getTitleType())
            .put("primaryTitle", movie.getPrimaryTitle())
            .put("originalTitle", movie.getOriginalTitle())
            .put("isAdult", movie.getIsAdult())
            .put("startYear", movie.getStartYear())
            .put("endYear", movie.getEndYear())
            .put("runtimeMinutes", movie.getRuntimesMinutes());
    }
}

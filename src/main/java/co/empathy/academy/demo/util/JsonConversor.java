package co.empathy.academy.demo.util;

import org.json.JSONObject;

import co.empathy.academy.demo.Models.Movie;

public class JsonConversor {
    //less the genre list. No used yet
    public static Movie jsontoMovie(JSONObject json){
        return new Movie(
                json.optString("id"),
                json.optString("type"),
                json.optString("primaryTitle"),
                json.optString("originalTitle"),
                json.optBoolean("isAdult"),
                json.optString("startYear"),
                json.optString("endYear"),
                json.optInt("runtimeMinutes"),
                json.optString("genre")
        );
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

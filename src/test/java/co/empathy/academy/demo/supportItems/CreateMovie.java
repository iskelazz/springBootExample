package co.empathy.academy.demo.supportItems;

import java.util.LinkedList;
import java.util.List;

import co.empathy.academy.demo.Models.Aka;
import co.empathy.academy.demo.Models.Director;
import co.empathy.academy.demo.Models.Movie;
import co.empathy.academy.demo.Models.Name;
import co.empathy.academy.demo.Models.Starring;

public class CreateMovie {
    public static Movie getGenericMovie(String id, String name) {
        return new Movie(id, "movie", name, name,
                false, 2020, "0", 121, new LinkedList<>(){{add ("Terror"); add("Comedy");}},
                4.5, 30000, getGenericAkas(), getGenericDirectors(), getGenericStarrings());
    }

    public static List<Aka> getGenericAkas(){
        return new LinkedList<>(){{
            add(getGenericAka("US"));
            add(getGenericAka("MX"));
        }}
        ;
    }

    public static Aka getGenericAka (String region){
        return new Aka("title",region,"language",false);
    }

    public static List<Director> getGenericDirectors(){
        return new LinkedList<>(){{
            add(getGenericDirector("Juan Jose"));
            add(getGenericDirector("Jose Juan"));
        }}
        ;
    }

    public static Starring getGenericStarring (String code){
        Name name = new Name(code);
        return new Starring(name,"Josito");
    }

    public static List<Starring> getGenericStarrings(){
        return new LinkedList<>(){{
            add(getGenericStarring("Rumba1"));
            add(getGenericStarring("Rumba2"));
        }}
        ;
    }

    public static Director getGenericDirector (String code){
        return new Director(code);
    }
}

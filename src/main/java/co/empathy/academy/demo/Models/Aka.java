package co.empathy.academy.demo.Models;

import lombok.*;

@With
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Value
public class Aka {
    String title;
    String region;
    String language;
    Boolean isOriginalTitle;
}

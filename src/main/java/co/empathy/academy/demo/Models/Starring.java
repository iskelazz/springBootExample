package co.empathy.academy.demo.Models;

import lombok.*;

@With
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Value
public class Starring {
    Name name;
    String characters;
}

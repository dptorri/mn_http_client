package example.model;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class Person {
    private Integer id;
    private String firstName;
    private String lastName;
    private int age;
}

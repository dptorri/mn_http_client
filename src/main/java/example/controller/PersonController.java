package example.controller;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import example.model.Person;
import io.micronaut.http.annotation.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller("persons")
public class PersonController {

    List<Person> persons = new ArrayList<>();

    @Post
    public Person save(@Body Person person) {
        person.setId(persons.size() + 1);
        persons.add(person);

        return person;
    }
}

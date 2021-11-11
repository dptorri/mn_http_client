package example.controller;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import example.model.Person;
import io.micronaut.http.annotation.Post;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller("/persons")
public class PersonController {

    List<Person> persons = new ArrayList<>();

    @Post
    public Person save(@Body @Valid Person person) {
        person.setId(persons.size() + 1);
        persons.add(person);

        return person;
    }

    @Get("/{id}")
    public Optional<Person> findById(@NotNull Integer id) {
        return persons.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Get("{?max,offset}")
    public List<Person> findAll(@Nullable Integer max, @Nullable Integer offset) {
        return persons.stream()
                .skip(offset == null ? 0 : offset)
                .limit(max == null ? 10000 : max)
                .collect(Collectors.toList());
    }
}

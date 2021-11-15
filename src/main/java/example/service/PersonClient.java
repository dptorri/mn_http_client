package example.service;

import example.model.Person;
import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import java.util.List;

@Client("/persons")
public interface PersonClient {

    @Version("2")
    @Get("?max,offset")
    List<Person> findAllV2(Integer max, Integer offset);
}

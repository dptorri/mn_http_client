package example.controller;

import example.model.Person;
import example.service.PersonClient;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.util.List;

@MicronautTest
public class PersonClientTest {

    @Inject
    PersonClient client;

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    public void testFindAllV2() throws MalformedURLException {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setAge(33);
        person = httpClient.toBlocking().retrieve(HttpRequest.POST("/persons", person), Person.class);
        Assertions.assertNotNull(person);

        List<Person> persons = client.findAllV2(10, 0);
        Assertions.assertEquals(1, persons.size());
    }
}
package example.controller;

import example.model.Person;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;


@MicronautTest
public class PersonControllerTests {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void testSave() throws MalformedURLException {
       Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setAge(33);
        person = client.toBlocking().retrieve(HttpRequest.POST("/persons", person), Person.class);
        Assertions.assertNotNull(person);
        Assertions.assertEquals("John", person.getFirstName());
        Assertions.assertEquals("Smith", person.getLastName());
    }

    @Test
    public void testSaveNotValid() throws MalformedURLException {
        final Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setAge(-1);

        Assertions.assertThrows(HttpClientResponseException.class,
            () -> client
                    .toBlocking()
                    .retrieve(HttpRequest.POST("/persons", person), Person.class),
            "person.age: must be greater than or equal to 0");
    }

    @Test
    public void testFindById() throws MalformedURLException {
        Person person = client.toBlocking().retrieve(HttpRequest.GET("/persons/1"), Person.class);
        Assertions.assertNotNull(person);
    }

    @Test
    public void testFindAll() throws MalformedURLException {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setAge(33);
        person = client.toBlocking().retrieve(HttpRequest.POST("/persons", person), Person.class);
        Assertions.assertNotNull(person);

        Person[] persons = client.toBlocking().retrieve(HttpRequest.GET("/persons"), Person[].class);
        Assertions.assertEquals(1, persons.length);
    }


}

package example.controller;

import example.model.Person;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;


@MicronautTest
public class PersonControllerTests {

    @Inject
    EmbeddedServer server;

    @Test
    public void testSave() throws MalformedURLException {
        HttpClient client = HttpClient
                .create(new URL("http://" + server.getHost() + ":" + server.getPort()));
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setAge(33);
        person = client.toBlocking().retrieve(HttpRequest.POST("/persons", person), Person.class);
        Assertions.assertNotNull(person);
        Assertions.assertEquals(1, person.getId());
        Assertions.assertEquals("John", person.getFirstName());
        Assertions.assertEquals("Smith", person.getLastName());
    }

}

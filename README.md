## Micronaut Http Client


### 0. Setup
```
 ./gradlew tasks run 
 ```

### 1. Create a Create project from CLI
#### add Reactor and graalvm
```
mn create-app --features=reactor,graalvm example.httpclient --build=gradle --lang=java   
```

### 2. Create the Person controller
#### 2.1 Create the Person model
```
Person {
 id: Integer
 firstName: String
 lastName: String
 age: int
}
```
#### 2.2 Implement methods for adding, finding all and finding a single person by id
````
POST /persons
GET /persons/{id}
GET /?max,offset
````
#### 2.3 Add validation for the Person model
```

implementation "io.micronaut:micronaut-validation"
implementation "io.micronaut.beanvalidation:micronaut-hibernate-validator:3.0.0"
---
Person {
 id: max 10.000
 firstName: (not blank),
 lastName: (not blank),
 age: Not null or negative
}
```
#### 2.4 Add some users with curl in a bash script
```
#!/bin/bash

curl -X POST "http://localhost:8100/persons" \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
--data-binary @- <<DATA
{
  "id": 2,
  "firstName": "Dejah",
  "lastName": "Thoris",
  "age": 44
}
DATA


```
### 3. Test using micronaut-http-client
#### Test using simple mock data in JUnit 5
#### 3.1 Add test for save endpoint
```
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
```
#### 3.2 Simplify HttpClient by using @Client annotation
```
HttpClient client = HttpClient
    .create(new URL("http://" + server.getHost() + ":" + server.getPort()));
    
// BETTER:

@Inject
@Client("/")
HttpClient client;
```
#### 3.3 Add test for save Not Valid endpoint
```
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
```
#### 3.4 Add test for testFindById
```
@Test
public void testFindById() throws MalformedURLException {
    Person person = client.toBlocking().retrieve(HttpRequest.GET("/persons/1"), Person.class);
    Assertions.assertNotNull(person);
}
```
#### 3.5 Add test for testFindAll
```

@Test
public void testFindById() throws MalformedURLException {
    Person person = client.toBlocking().retrieve(HttpRequest.GET("/persons/1"), Person.class);
    Assertions.assertNotNull(person);
}
```
##### 4 API versioning
#### 4.1. Create a version 2 of the findAll endpoint
```
@Version("1")
@Get("{?max,offset}")
...
}

@Version("2")
@Get("?max,offset")
...
}
// We will access the new endpoint with the Declarative Client PersonClient   

@Client("/persons")
public class PersonClient {
    @Get("?max,offset")
    List<Person> findAllV2(Integer max, Integer offset); 
}
// PersonClientTest.java
// Push a new record and retrieve with testFindAllV2
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
```
##### 5 Headers and Query parameters
#### 5.1. Retrieve username from paramsController

```
@Get("/usernameList/{username}")
public String usernameList(String username) {
    return String.format("Query parameter username = '%s'", username);
}
------
❯ curl -X GET http://localhost:8100/params/usernameList/dodos/
Query parameter username = 'dodos'%  
```

## Micronaut Http Client

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

### 3. Test using micronaut-http-client
#### Test using simple mock data in JUnit 5
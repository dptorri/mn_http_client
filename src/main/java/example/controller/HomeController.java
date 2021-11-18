package example.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/")
public class HomeController {

    @Get
    public String home(){
        return "MN_HTTP_CLIENT PORT 8100 /params/getAuthorizationHeader";
    }
}

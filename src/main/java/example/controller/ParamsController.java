package example.controller;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.context.ServerRequestContext;
import reactor.core.publisher.Mono;


@Controller("/params")
public class ParamsController {

    @Get("/usernameList/{username}")
    public String usernameList(String username) {
        return String.format("Query parameter username = '%s'", username);
    }

    @Get("/getHelloHeader")
    public Mono<HttpResponse<String>> getHelloHeader() {
        return Mono.deferContextual(ctx -> {
            HttpRequest<?> request = ctx.get(ServerRequestContext.KEY);
            String name = request.getParameters()
                    .getFirst("name")
                    .orElse("Nobody");

            return Mono.just(HttpResponse.ok("Hello there " + name + "!!")
                    .header("X-Hello-Header", "Hello"));
        });
    }

    @Get("/getAllHeaderValues")
    public HttpResponse<String> getAllHeaderValues(HttpHeaders headers) {
        return HttpResponse.ok(String.format("HEADERS = '%s'", headers.values()));
    }

    @Get("/getAuthorizationHeader")
    public HttpResponse<String> getAuthorizationHeader(HttpHeaders headers) {
        String authorizationHeader;

        if(headers.findFirst(HttpHeaders.AUTHORIZATION).isPresent()) {
            authorizationHeader = headers.findFirst(HttpHeaders.AUTHORIZATION).get();
        } else {
            authorizationHeader = "Authorization Header Not Found";
        }

        return HttpResponse.ok(
                String.format("Authorization Header = %s", authorizationHeader));
    }

}

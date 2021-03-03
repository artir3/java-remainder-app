package dev.arturmarkowski.reaminderapp.endpoints;

import dev.arturmarkowski.reaminderapp.Main;
import dev.arturmarkowski.remainderapp.models.Greeting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/*
this endpoint was created based onb this site:
https://spring.io/guides/gs/consuming-rest/
 */
@RestController()
public class ConsumingRESTFullApiController {

    @Autowired
    private RestConsumer restConsumer;

    @GetMapping(value = "/api/consuming/restconsumer")
    public String get(@RequestParam(name = "path",defaultValue = "http://127.0.0.1:8080/api/building/greeting") String path) {
        return restConsumer.run(path);
    }


}

@Controller
class RestConsumer {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private final RestTemplate restTemplate;

    RestConsumer(RestTemplateBuilder builder) {
        this.restTemplate = this.restTemplate(builder);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public  String run(String url) {
        var result = "Default request";
        try {
            var quote = Optional.ofNullable(restTemplate.getForObject(url, Greeting.class));
            result = quote.isPresent() ? quote.toString() : result;
        } catch (HttpClientErrorException e) {
            log.error("Given URL can not be reached", e);
        } catch (Exception e) {
            log.error("Some errors in the consuming URL", e);
        }
        return result;
    }

}
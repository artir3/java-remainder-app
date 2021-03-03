package dev.arturmarkowski.reaminderapp.endpoints;

import dev.arturmarkowski.reaminderapp.Main;
import dev.arturmarkowski.remainderapp.models.Greeting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumingRESTFullApiController {

    private final RestConsumer restConsumer;

    public ConsumingRESTFullApiController(RestConsumer restConsumer) {
        this.restConsumer = restConsumer;
    }

    @GetMapping(value = "/restconsumer")
    public String get(@RequestParam(name = "path",defaultValue = "http://127.0.0.1:8080/greeting") String path) {
        return restConsumer.run(path, dev.arturmarkowski.remainderapp.models.Greeting.class);
    }


}

@Controller
class RestConsumer {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private RestTemplate restTemplate;

    RestConsumer(RestTemplateBuilder builder) {
        this.restTemplate = this.restTemplate(builder);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public  String run(String url, Class type) {
        var result = "Default request";
        try {
            var quote = restTemplate.getForObject(url, type);
            result = quote.toString();
        } catch (HttpClientErrorException e) {
            log.error("Given URL can not be reached", e);
        } catch (Exception e) {
            log.error("Some errors in the consuming URL", e);
        }
        return result;
    }

}
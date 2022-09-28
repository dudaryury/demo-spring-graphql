package com.demo.graphql.demographql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@SpringBootApplication
public class DemoGraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoGraphqlApplication.class, args);
    }

    @Controller
    public class GreetingsController {

        @SubscriptionMapping
        public Flux<Greeting> greetings() {
            return Flux.fromStream(Stream.generate(() -> new Greeting("Hello World @ " + Instant.now())))
                    .delayElements(Duration.ofSeconds(1))
                    .take(5);
        }

        @QueryMapping
        public Greeting greeting() {
            return new Greeting("Hello World");
        }
    }

    public static class Greeting {
        private final String greeting;

        public Greeting(String greeting) {
            this.greeting = greeting;
        }

        public String getGreeting() {
            return greeting;
        }
    }
}

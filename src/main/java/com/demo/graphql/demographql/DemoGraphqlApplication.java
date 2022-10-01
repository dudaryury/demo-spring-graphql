package com.demo.graphql.demographql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@SpringBootApplication
public class DemoGraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoGraphqlApplication.class, args);
    }

    @Controller
    class GreetingsController {
        private final List<Customer> CUSTOMERS = List.of(new Customer(1, "Serega"), new Customer(2, "Ivan"), new Customer(3, "Yura"));

        @QueryMapping
        String helloWithName(@Argument String name) {
            return "Hello, " + name + "!";
        }


        @QueryMapping
            //@SchemaMapping(typeName = "Query", field = "hello")
        String hello() {
            return "Hello, world!";
        }

        @QueryMapping
        Customer customerById(@Argument Integer id) {
            return CUSTOMERS.stream().filter(customer -> Objects.equals(customer.id, id)).findFirst().orElseThrow();
        }

        @QueryMapping
            //or Flux<Customer>
        List<Customer> customers() {
            return CUSTOMERS;
        }

        @SubscriptionMapping
        Flux<Customer> customersSubscription() {
            return Flux.fromStream(Stream.generate(() -> new Customer(1, Instant.now().toString())))
                    .delayElements(Duration.ofSeconds(1))
                    .take(5);
        }
    }

    public static class Customer {
        private Integer id;
        private String name;

        public Customer(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Customer() {
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Customer{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}

package com.demo.graphql.demographql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoGraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoGraphqlApplication.class, args);
    }

    @Controller
    class GraphController {
        private final List<Customer> CUSTOMERS = List.of(new Customer(1, "Serega"), new Customer(2, "Ivan"), new Customer(3, "Yura"));

        @QueryMapping
            //or Flux<Customer>
        List<Customer> customers() {
            return CUSTOMERS;
        }

        @BatchMapping
        Map<Customer, Account> account(List<Customer> customers) {
            return customers.stream()
                    .collect(Collectors.toMap(Function.identity(),
                            customer -> new Account(customer.id)));
        }

/*        @SchemaMapping(typeName = "Customer")
        Account account(Customer customer) {
            return new Account(customer.id);
        }*/
    }

    public static class Customer {
        Integer id;
        String name;

        public Customer(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static class Account {
        Integer id;

        public Account(Integer id) {
            this.id = id;
        }
    }
}

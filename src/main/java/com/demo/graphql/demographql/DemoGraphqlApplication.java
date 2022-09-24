package com.demo.graphql.demographql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class DemoGraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoGraphqlApplication.class, args);
    }

    @Controller
    class MutationController {
        private final List<Customer> CUSTOMERS = new ArrayList<>() {
            {
                add(new Customer(1, "Serega"));
                add(new Customer(2, "Ivan"));
                add(new Customer(3, "Yura"));
            }
        };

        private final AtomicInteger ID_GENERATOR = new AtomicInteger(4);


        @QueryMapping
            //or Flux<Customer>
        List<Customer> customers() {
            return CUSTOMERS;
        }

        @QueryMapping
        Customer customerById(@Argument Integer id) {
            return CUSTOMERS.stream().filter(customer -> Objects.equals(customer.id, id)).findFirst().orElse(null);
        }

        @MutationMapping
            //@SchemaMapping(typeName = "Mutation", field = "addCustomer")
        Customer addCustomer(@Argument String name) {
            var id = ID_GENERATOR.incrementAndGet();
            Customer customer = new Customer(id, name);
            CUSTOMERS.add(customer);
            return customer;
        }
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

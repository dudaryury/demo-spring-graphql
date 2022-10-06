package com.demo.graphql.demographql;

import com.demo.graphql.demographql.db.Customer;
import com.demo.graphql.demographql.db.CustomerRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class DemoGraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoGraphqlApplication.class, args);
    }

    //!!! low-level configuration with spring-data-jpa
/*    @Bean
    RuntimeWiringConfigurer runtimeWiringConfigurer(CustomerRepository customerRepository) {
        return builder -> {
            var datum = QuerydslDataFetcher.builder(customerRepository).single();
            var data = QuerydslDataFetcher.builder(customerRepository).many();
            builder.type("Query", wiring -> wiring
                    .dataFetcher("customer", datum)
                    .dataFetcher("customers", data)
                    .dataFetcher("customersByName", data)
            );
        };
    }*/

    @Bean
    ApplicationRunner applicationRunner(CustomerRepository customerRepository) {
        return args -> customerRepository.saveAll(List.of(
                new Customer(1, "Yura"),
                new Customer(2, "Olya"),
                new Customer(3, "Roma"),
                new Customer(4, "Marina")
        ));
    }
}

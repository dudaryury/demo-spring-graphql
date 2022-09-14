package com.demo.graphql.demographql.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Configuration
public class GraphQlConfiguration {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(CrmService crmService) {
        return builder -> {
            builder.type("Customer", wiring -> wiring
                    .dataFetcher("profile", env -> crmService.getProfileFor(env.getSource())));

            builder.type("Query", wiring -> wiring
                    .dataFetcher("customerById", env -> crmService.getCustomerById(Integer.parseInt(env.getArgument("id"))))
                    .dataFetcher("customers", env -> crmService.getCustomers()));
        };
    }

    @Service
    public class CrmService {
        private final List<Customer> CUSTOMERS = List.of(new Customer(1, "Serega"), new Customer(2, "Ivan"), new Customer(3, "Yura"));

        public Customer getCustomerById(Integer id) {
            return CUSTOMERS.stream().filter(customer -> Objects.equals(customer.id, id)).findFirst().orElseThrow();
        }

        public Collection<Customer> getCustomers() {
            return CUSTOMERS;
        }

        public Profile getProfileFor(Customer customer) {
            return new Profile(customer.id, customer.id);
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

    public static class Profile {
        Integer id;
        Integer customerId;

        public Profile(Integer id, Integer customerId) {
            this.id = id;
            this.customerId = customerId;
        }
    }
}

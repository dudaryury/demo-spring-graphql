package com.demo.graphql.demographql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@EnableReactiveMethodSecurity
@SpringBootApplication
public class DemoGraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoGraphqlApplication.class, args);
    }

    @Bean
    SecurityWebFilterChain authorization(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorize -> authorize.anyExchange().permitAll())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    MapReactiveUserDetailsService authentication() {
        var users = Map.of(
                        "user", new String[]{"USER"},
                        "admin", new String[]{"USER", "ADMIN"}
                ).entrySet().stream()
                .map(entry -> User.builder()
                        .username(entry.getKey())
                        .password("test")
                        .roles(entry.getValue())
                        .build()
                ).collect(Collectors.toList());

        return new MapReactiveUserDetailsService(users);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Controller
    class SecureGrapqlController {
        private final List<Customer> CUSTOMERS = new ArrayList<>() {
            {
                new Customer(1, "Serega");
                new Customer(2, "Ivan");
                new Customer(3, "Yura");
            }
        };

        private final AtomicInteger ID_GENERATOR = new AtomicInteger(3);

        @Secured("ROLE_USER")
        @QueryMapping
        Mono<Customer> customerById(@Argument Integer id) {
            return Flux.fromIterable(CUSTOMERS)
                    .filter(customer -> Objects.equals(customer.id, id))
                    .next();
        }

        @QueryMapping
        Flux<Customer> customers() {
            return Flux.fromIterable(CUSTOMERS);
        }

        @PreAuthorize("hasRole('ADMIN')")
        @MutationMapping
        Mono<Customer> insert(@Argument String name) {
            Customer customer = new Customer(ID_GENERATOR.incrementAndGet(), name);
            CUSTOMERS.add(customer);
            return Mono.just(customer);
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
}

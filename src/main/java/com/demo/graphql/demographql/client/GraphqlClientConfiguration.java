package com.demo.graphql.demographql.client;

import com.demo.graphql.demographql.DemoGraphqlApplication;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.client.RSocketGraphQlClient;

@Configuration
public class GraphqlClientConfiguration {

    @Bean
    public RSocketGraphQlClient rSocketGraphQlClient(RSocketGraphQlClient.Builder<?> rsocketBuilder) {
        return rsocketBuilder.tcp("127.0.0.1", 9191).route("graphql").build();
    }

    @Bean
    public HttpGraphQlClient httpGraphQlClient() {
        return HttpGraphQlClient.builder().url("http://127.0.0.1:8080/graphql").build();
    }

    @Bean
    public ApplicationRunner applicationRunner(RSocketGraphQlClient rSocketGraphQlClient, HttpGraphQlClient httpGraphQlClient) {
        return args -> {
            var httpRequest = "query {\n" +
                    "  customerById(id: 1) {\n" +
                    "    id, name\n" +
                    "  }\n" +
                    "}";

            httpGraphQlClient.document(httpRequest)
                    .retrieve("customerById")
                    .toEntity(DemoGraphqlApplication.Customer.class)
                    .subscribe(System.out::println);

            var rsocketRequest = "subscription {" +
                    "   customersSubscription {" +
                    "           id, name" +
                    "   }" +
                    "}";

            rSocketGraphQlClient.document(rsocketRequest)
                    .retrieveSubscription("customersSubscription")
                    .toEntity(DemoGraphqlApplication.Customer.class)
                    .subscribe(System.out::println);
        };
    }
}

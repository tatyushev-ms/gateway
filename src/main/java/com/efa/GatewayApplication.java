package com.efa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
public class GatewayApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
    
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration c) {
        return builder.routes()
                
                .route("auth-service", r -> r.path("/oauth/**").uri(c.getAuthServiceUri()))
                
                .route("order-service", r -> r.path("/api/{api-version}/orders/**").or().path("/api/{api-version}/invoices/**")
                        .filters(f -> f.setRequestHeader("X-Api-Version", "{api-version}"))
                        .uri(c.getOrderServiceUri()))
                
                .build();
    }
    
}

package com.wilfred.apigateway.apigateway.routes;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
public class Routes {
    @Value("${routes.url.order-service}")
    private String orderServiceEndpoint;

    @Value("${routes.url.product-service}")
    private String productServiceEndpoint;

    @Value("${routes.url.inventory-service}")
    private String inventoryServiceEndpoint;

    @Bean
    public RouterFunction<ServerResponse> responseProductService() {

        return route("product_service").route(RequestPredicates.path("/api/v1/product"), HandlerFunctions.http(productServiceEndpoint))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> responseProductServiceSwaggerConfig() {

        return route("product_service_swagger").route(RequestPredicates.path("/aggregate/product-service/v1/api-docs"), HandlerFunctions.http(productServiceEndpoint))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceSwaggerCircuitBreaker",
                        URI.create("forward:/fallbackRoute"))).
                filter(setPath("/api-docs")).build();
    }

    @Bean
    public RouterFunction<ServerResponse> responseOrderService() {
        return route("order_service").route(RequestPredicates.path("/api/v1/orders"), HandlerFunctions.http(orderServiceEndpoint))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> responseOrderServiceSwaggerConfig() {
        return route("order_service_swagger").route(RequestPredicates.path("/aggregate/order-service/v1/api-docs"), HandlerFunctions.http(orderServiceEndpoint))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceSwaggerCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> responseInventoryService() {
        return route("inventory_service").route(RequestPredicates.path("/api/v1/inventories"), HandlerFunctions.http(inventoryServiceEndpoint))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> responseInventoryServiceSwaggerConfig() {
        return route("inventory_service_swagger").route(RequestPredicates.path("/aggregate/inventory-service/v1/api-docs"), HandlerFunctions.http(inventoryServiceEndpoint))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceSwaggerCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute").
                GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service Unavailable, Please try again later!")).build();
    }
}

package com.wilfred.apigateway.apigateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;

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

        return GatewayRouterFunctions.route("product_service").route(RequestPredicates.path("/api/v1/product"), HandlerFunctions.http(productServiceEndpoint)).build();
    }

    @Bean
    public RouterFunction<ServerResponse> responseProductServiceSwaggerConfig() {

        return GatewayRouterFunctions.route("product_service_swagger").route(RequestPredicates.path("/aggregate/product-service/v1/api-docs"), HandlerFunctions.http(productServiceEndpoint)).filter(setPath("/api-docs")).build();
    }

    @Bean
    public RouterFunction<ServerResponse> responseOrderService() {
        return GatewayRouterFunctions.route("order_service").route(RequestPredicates.path("/api/v1/orders"), HandlerFunctions.http(orderServiceEndpoint)).build();
    }

    @Bean
    public RouterFunction<ServerResponse> responseOrderServiceSwaggerConfig() {
        return GatewayRouterFunctions.route("order_service_swagger").route(RequestPredicates.path("/aggregate/order-service/v1/api-docs"), HandlerFunctions.http(orderServiceEndpoint))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> responseInventoryService() {
        return GatewayRouterFunctions.route("inventory_service").route(RequestPredicates.path("/api/v1/inventories"), HandlerFunctions.http(inventoryServiceEndpoint)).build();
    }

    @Bean
    public RouterFunction<ServerResponse> responseInventoryServiceSwaggerConfig() {
        return GatewayRouterFunctions.route("inventory_service_swagger").route(RequestPredicates.path("/aggregate/inventory-service/v1/api-docs"), HandlerFunctions.http(inventoryServiceEndpoint))
                .filter(setPath("/api-docs"))
                .build();
    }
}

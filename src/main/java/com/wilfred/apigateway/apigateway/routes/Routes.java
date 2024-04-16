package com.wilfred.apigateway.apigateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

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

        return GatewayRouterFunctions.route("product-service")
                .route(RequestPredicates.path("/api/v1/product"), HandlerFunctions.http(orderServiceEndpoint))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> responseOrderService() {
        return GatewayRouterFunctions.route("order-service")
                .route(RequestPredicates.path("/api/v1/orders"), HandlerFunctions.http(productServiceEndpoint))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> responseInventoryService() {
        return GatewayRouterFunctions.route("inventory-service")
                .route(RequestPredicates.path("/api/v1/inventories"), HandlerFunctions.http(inventoryServiceEndpoint))
                .build();
    }

}

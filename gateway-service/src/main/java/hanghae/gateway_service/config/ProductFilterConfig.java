package hanghae.gateway_service.config;

import static hanghae.gateway_service.util.GatewayConstant.PRODUCT_ROUTE_ID;
import static hanghae.gateway_service.util.GatewayConstant.PRODUCT_SERVICE_URI;
import static hanghae.gateway_service.util.GatewayConstant.REWRITE_PATH_REPLACEMENT;
import static hanghae.gateway_service.util.GatewayConstant.createRewritePathPattern;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductFilterConfig {

    @Bean
    public RouteLocator productRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(PRODUCT_ROUTE_ID, r -> r.path("/product-service/**")
                        .filters(f -> f.removeRequestHeader("Cookie")
                                .rewritePath(createRewritePathPattern(PRODUCT_ROUTE_ID), REWRITE_PATH_REPLACEMENT)
                        )
                        .uri(PRODUCT_SERVICE_URI))
                .build();
    }
}

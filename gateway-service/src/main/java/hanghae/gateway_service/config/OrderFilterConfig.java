package hanghae.gateway_service.config;

import static hanghae.gateway_service.util.GatewayConstant.ORDER_ROUTE_ID;
import static hanghae.gateway_service.util.GatewayConstant.ORDER_SERVICE_URI;
import static hanghae.gateway_service.util.GatewayConstant.REWRITE_PATH_REPLACEMENT;
import static hanghae.gateway_service.util.GatewayConstant.USER_ROUTE_ID;
import static hanghae.gateway_service.util.GatewayConstant.createRewritePathPattern;

import hanghae.gateway_service.filter.AuthorizationHeaderFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderFilterConfig {

    @Bean
    public RouteLocator cartRoutes(RouteLocatorBuilder builder, AuthorizationHeaderFilter authFilter) {
        return builder.routes()
                .route(ORDER_ROUTE_ID, r -> r.path("/order-service/cart/**")
                        .filters(f -> f.removeRequestHeader("Cookie")
                                .rewritePath(createRewritePathPattern(USER_ROUTE_ID), REWRITE_PATH_REPLACEMENT)
                                .filter(authFilter.apply(new AuthorizationHeaderFilter.Config()))
                        )
                        .uri(ORDER_SERVICE_URI))
                .build();
    }

    @Bean
    public RouteLocator orderRoutes(RouteLocatorBuilder builder, AuthorizationHeaderFilter authFilter) {
        return builder.routes()
                .route(ORDER_ROUTE_ID, r -> r.path("/order-service/order/**")
                        .filters(f -> f.removeRequestHeader("Cookie")
                                .rewritePath(createRewritePathPattern(USER_ROUTE_ID), REWRITE_PATH_REPLACEMENT)
                                .filter(authFilter.apply(new AuthorizationHeaderFilter.Config()))
                        )
                        .uri(ORDER_SERVICE_URI))
                .build();
    }


}

package hanghae.gateway_service.config;

import static hanghae.gateway_service.util.GatewayConstant.*;

import hanghae.gateway_service.filter.AuthorizationHeaderFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class UserFilterConfig {

    @Bean
    public RouteLocator signupRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(USER_ROUTE_ID, r -> r.path("/user-service/signup", "/user-service/login")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(f -> f.removeRequestHeader("Cookie")
                                .rewritePath(createRewritePathPattern(USER_ROUTE_ID), REWRITE_PATH_REPLACEMENT))
                        .uri(USER_SERVICE_URI))
                .build();
    }

    @Bean
    public RouteLocator userRoutes(RouteLocatorBuilder builder, AuthorizationHeaderFilter authFilter) {
        return builder.routes()
                .route(USER_ROUTE_ID, r -> r.path("/user-service/signup", "/user-service/login")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(f -> f.removeRequestHeader("Cookie")
                                .rewritePath(createRewritePathPattern(USER_ROUTE_ID), REWRITE_PATH_REPLACEMENT)
                                .filter(authFilter.apply(new AuthorizationHeaderFilter.Config()))
                        )
                        .uri(USER_SERVICE_URI))
                .build();
    }


}

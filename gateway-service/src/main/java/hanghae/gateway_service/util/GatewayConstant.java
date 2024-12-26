package hanghae.gateway_service.util;

public class GatewayConstant {

    // user
    public static final String USER_ROUTE_ID = "user-service";
    public static final String USER_SERVICE_URI = "lb://USER-SERVICE";

    // order
    public static final String ORDER_ROUTE_ID = "order-service";
    public static final String ORDER_SERVICE_URI = "lb://ORDER-SERVICE";

    // product
    public static final String PRODUCT_ROUTE_ID = "product-service";
    public static final String PRODUCT_SERVICE_URI = "lb://PRODUCT-SERVICE";

    // common
    public static final String REWRITE_PATH_REPLACEMENT = "/${segment}";

    public static String createRewritePathPattern(String routeId) {
        return String.format("/%s/(?<segment>.*)", routeId);
    }


}

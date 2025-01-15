package hanghae.order_service.service.common.util;

import java.time.LocalTime;

public class OrderConstant {

    public static final int RETURN_DAY = 1;
    public static final int ORDER_SUCCESS = 1;
    public static final int ORDER_FAIL = -1;

    public static final String SHOW_ORDER_ID = "주문번호: ";
    public static final String SHOW_PRODUCT_ID = "상품번호: ";

    public static final LocalTime OPEN_TIME = LocalTime.of(9, 0);
}

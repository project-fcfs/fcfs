package hanghae.user_service.service.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.user_service.controller.resp.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class CustomResponseUtil {

    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    public static void success(HttpServletResponse response, String message) {
        try{
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(1, message,null);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(responseBody);
        } catch (Exception e) {
            log.error("서버 파싱 에러 {}",e.getMessage());
        }
    }

    public static void fail(HttpServletResponse response, String message, HttpStatus status) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<Object> responseDto = new ResponseDto<>(-1, message, null);
            String responseBody = om.writeValueAsString(responseDto);
            response.setStatus(status.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(responseBody);

        } catch (Exception e) {
            log.error("서버 파싱 에러", e);
        }
    }
}

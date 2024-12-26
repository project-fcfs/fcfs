package hanghae.product_service.controller.resp;

public record ResponseDto<T>(
        int code,
        String message,
        T data
) {

    public static<T> ResponseDto<?> success(T data){
        return new ResponseDto<>(1, "success", data);
    }
}

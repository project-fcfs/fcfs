package hanghae.product_service.controller.req;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "파일 정보를 담고 있는 DTO")
public record FileInfo(
        @Schema(description = "원본 파일 이름", example = "image.jpg")
        String originalName,

        @Schema(description = "파일의 Content Type", example = "image/jpeg")
        String contentType,

        @Schema(description = "파일 크기 (바이트 단위)", example = "102400")
        long size,

        @Schema(description = "파일 입력 스트림", hidden = true)
        InputStream inputStream
) {
    public static FileInfo create(MultipartFile file) throws IOException {
        return new FileInfo(file.getOriginalFilename(), file.getContentType(),
                file.getSize(), file.getInputStream());
    }
}

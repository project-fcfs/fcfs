package hanghae.product_service.controller.req;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public record FileInfo(
        String originalName,
        String contentType,
        long size,
        InputStream inputStream
) {
    public static FileInfo create(MultipartFile file) throws IOException {
        return new FileInfo(file.getOriginalFilename(), file.getContentType(),
                file.getSize(), file.getInputStream());
    }
}

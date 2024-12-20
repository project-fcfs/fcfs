package hanghae.product_service.controller.common;

import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

public class MultipartUtil {

    private static final String INVALID_FILE_IMAGE = "유효하지 않은 파일 확장자입니다.";

    private static final Set<String> ALLOWED_IMAGE_FORMATS = Set.of("jpg", "jpeg", "png", "gif", "webp");

    public static void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return;
        }
        String fileName = file.getOriginalFilename();

        int extIndex = fileName.lastIndexOf(".");
        if (extIndex == -1) {
            throw new IllegalArgumentException(INVALID_FILE_IMAGE);
        }
        String ext = fileName.substring(extIndex + 1).toLowerCase();
        if (!ALLOWED_IMAGE_FORMATS.contains(ext)) {
            throw new IllegalArgumentException(INVALID_FILE_IMAGE);
        }
    }
}

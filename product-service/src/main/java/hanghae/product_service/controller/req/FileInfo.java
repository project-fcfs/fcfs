package hanghae.product_service.controller.req;

import hanghae.product_service.domain.imagefile.PhotoType;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record FileInfo(
        PhotoType photoType,
        List<MultipartFile> files
) {
}

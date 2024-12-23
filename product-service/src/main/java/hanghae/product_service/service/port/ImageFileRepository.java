package hanghae.product_service.service.port;

import hanghae.product_service.domain.imagefile.ImageFile;
import java.util.Optional;

public interface ImageFileRepository {
    ImageFile save(ImageFile imageFile);
    Optional<ImageFile> fetchByProductId(Long productId);
}

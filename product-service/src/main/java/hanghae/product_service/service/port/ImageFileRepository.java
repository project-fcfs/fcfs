package hanghae.product_service.service.port;

import hanghae.product_service.domain.imagefile.ImageFile;
import java.util.Optional;

public interface ImageFileRepository {
    void save(ImageFile imageFile);
    Optional<ImageFile> fetchByProductId(Long productId);
}

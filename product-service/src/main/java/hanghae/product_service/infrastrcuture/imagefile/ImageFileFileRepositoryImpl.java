package hanghae.product_service.infrastrcuture.imagefile;

import hanghae.product_service.domain.imagefile.ImageFile;
import hanghae.product_service.service.port.ImageFileRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ImageFileFileRepositoryImpl implements ImageFileRepository {
    private final ImageJpaRepository jpaRepository;

    public ImageFileFileRepositoryImpl(ImageJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(ImageFile imageFile) {
        jpaRepository.save(ImageFileEntity.fromModel(imageFile));
    }

    @Override
    public Optional<ImageFile> fetchByProductId(Long productId) {
        return jpaRepository.fetchByProductId(productId).map(ImageFileEntity::toModel);
    }
}

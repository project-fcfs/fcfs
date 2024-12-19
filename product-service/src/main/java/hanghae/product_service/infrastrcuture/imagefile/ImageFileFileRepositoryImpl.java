package hanghae.product_service.infrastrcuture.imagefile;

import hanghae.product_service.service.port.ImageFileRepository;
import java.awt.Image;
import org.springframework.stereotype.Repository;

@Repository
public class ImageFileFileRepositoryImpl implements ImageFileRepository {
    private final ImageJpaRepository jpaRepository;

    public ImageFileFileRepositoryImpl(ImageJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Image image) {

    }
}

package hanghae.product_service.infrastrcuture.imagefile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageJpaRepository extends JpaRepository<ImageFileEntity, Long> {
}

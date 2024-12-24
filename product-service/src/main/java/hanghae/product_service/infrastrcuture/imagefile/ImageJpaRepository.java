package hanghae.product_service.infrastrcuture.imagefile;

import hanghae.product_service.domain.imagefile.ImageFile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageJpaRepository extends JpaRepository<ImageFileEntity, Long> {
    @Query("select if from ImageFileEntity if join if.productEntity pe where pe.id = :id")
    Optional<ImageFileEntity> fetchByProductId(@Param("id") Long productId);

    @Query("select if from ImageFileEntity if join if.productEntity pe where pe.id in :productIds ")
    List<ImageFile> findAllInProductId(@Param("productIds") List<Long> productIds);
}

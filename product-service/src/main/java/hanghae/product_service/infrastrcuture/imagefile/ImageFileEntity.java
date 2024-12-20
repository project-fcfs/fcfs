package hanghae.product_service.infrastrcuture.imagefile;

import hanghae.product_service.domain.imagefile.ImageFile;
import hanghae.product_service.domain.imagefile.ImageFileStatus;
import hanghae.product_service.infrastrcuture.product.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Table(name = "image_file")
@Entity
public class ImageFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String originalName;
    @Column(nullable = false)
    private String storeFileName;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ImageFileStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    protected ImageFileEntity() {
    }

    public ImageFileEntity(Long id, String originalName, String storeFileName, ImageFileStatus status,
                           ProductEntity productEntity) {
        this.id = id;
        this.originalName = originalName;
        this.storeFileName = storeFileName;
        this.status = status;
        this.productEntity = productEntity;
    }

    public static ImageFileEntity fromModel(ImageFile imageFile) {
        return new ImageFileEntity(imageFile.id(), imageFile.originalName(),
                imageFile.storeFileName(), imageFile.status(),
                ProductEntity.fromModel(imageFile.product()));
    }

    public ImageFile toModel(){
        return new ImageFile(id, originalName, storeFileName, status, productEntity.toModel());
    }
}

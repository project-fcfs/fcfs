package hanghae.product_service.service;

import hanghae.product_service.controller.req.FileInfo;
import hanghae.product_service.domain.imagefile.ImageFile;
import hanghae.product_service.domain.product.Product;
import hanghae.product_service.service.port.ImageFileRepository;
import hanghae.product_service.service.port.UUIDRandomHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ImageFileService {

    private static final String IMAGE_EXTENSION = ".webp";
    private final ImageFileRepository imageFileRepository;
    private final UUIDRandomHolder uuidRandomHolder;

    public ImageFileService(ImageFileRepository imageFileRepository, UUIDRandomHolder uuidRandomHolder) {
        this.imageFileRepository = imageFileRepository;
        this.uuidRandomHolder = uuidRandomHolder;
    }

    @Transactional
    public void upload(Product savedProduct, FileInfo fileInfo) {
        if (fileInfo == null) {
            return;
        }
        String storeFileName = storeFileName();
        ImageFile imageFile = makeImageFile(savedProduct, storeFileName, fileInfo.originalName());
        imageFileRepository.save(imageFile);

    }

    private String storeFileName() {
        String uuid = uuidRandomHolder.getRandomUUID();
        return uuid + IMAGE_EXTENSION;
    }

    private ImageFile makeImageFile(Product product, String storeFileName, String originalFileName) {
        return ImageFile.create(originalFileName, storeFileName, product);
    }
}

package hanghae.product_service.mock;

import hanghae.product_service.domain.imagefile.ImageFile;
import hanghae.product_service.service.port.ImageFileRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeImageFileRepository implements ImageFileRepository {
    private List<ImageFile> data = new ArrayList<>();
    private AtomicLong counter = new AtomicLong();

    @Override
    public void save(ImageFile imageFile) {
        if (imageFile.id() == null || imageFile.id() == 0L) {
            ImageFile newImageFile = new ImageFile(counter.incrementAndGet(),
                    imageFile.originalName(), imageFile.storeFileName(), imageFile.status(),
                    imageFile.product());
            data.add(newImageFile);
        } else {
            data.removeIf(i -> i.id().equals(imageFile.id()));
            data.add(imageFile);
        }
    }

    @Override
    public Optional<ImageFile> fetchByProductId(Long productId) {
        return data.stream().filter(i -> i.product().id().equals(productId)).findFirst();
    }

}

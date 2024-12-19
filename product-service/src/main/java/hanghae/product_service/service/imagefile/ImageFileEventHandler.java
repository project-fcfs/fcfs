package hanghae.product_service.service.imagefile;

import hanghae.product_service.domain.imagefile.ImageFileCreateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ImageFileEventHandler {

    private final ImageFileService imageFileService;

    public ImageFileEventHandler(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }

    @EventListener
    public void save(ImageFileCreateEvent event) {

    }
}

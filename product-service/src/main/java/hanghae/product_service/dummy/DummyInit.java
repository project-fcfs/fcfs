package hanghae.product_service.dummy;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DummyInit {

    private final InitService initService;

    public DummyInit(InitService initService) {
        this.initService = initService;
    }

    @PostConstruct
    public void init(){
        initService.normalInit();
        initService.LimitedInit();
    }
}

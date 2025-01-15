package hanghae.user_service.dummy;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DummyInit {

    private final InitService0 initService0;

    public DummyInit(InitService0 initService0) {
        this.initService0 = initService0;
    }


    @PostConstruct
    public void testInit() {
        initService0.dbInit2();
    }

}


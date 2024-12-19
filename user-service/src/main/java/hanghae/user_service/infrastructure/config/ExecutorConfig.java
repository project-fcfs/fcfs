package hanghae.user_service.infrastructure.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class ExecutorConfig {

    @Bean("customTaskExecutor")
    public Executor taskExecutor(){
        return Executors.newFixedThreadPool(100);
    }
}

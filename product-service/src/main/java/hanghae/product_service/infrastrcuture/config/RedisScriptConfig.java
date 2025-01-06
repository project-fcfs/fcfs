package hanghae.product_service.infrastrcuture.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;

@Configuration
public class RedisScriptConfig {

    @Bean
    public RedisScript<Boolean> removeStockScript() {
        ClassPathResource scriptSource = new ClassPathResource("scripts/removeStock.lua");
        return RedisScript.of(scriptSource, Boolean.class);
    }

    @Bean
    public RedisScript<Boolean> restoreStockScript() {
        ClassPathResource scriptSource = new ClassPathResource("scripts/restoreStock.lua");

        return RedisScript.of(scriptSource, Boolean.class);
    }
}

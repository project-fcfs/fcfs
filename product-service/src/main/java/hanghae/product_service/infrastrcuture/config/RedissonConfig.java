package hanghae.product_service.infrastrcuture.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    private static final Logger log = LoggerFactory.getLogger(RedissonConfig.class);
    private final RedisClusterProperties prop;

    public RedissonConfig(RedisClusterProperties prop) {
        this.prop = prop;
    }

    @Bean
    public RedissonClient redissonClient() {
        final Config config = new Config();

        log.debug("redisson cluster {}", prop.getNodes());
        ClusterServersConfig csc = config.useClusterServers()
                .setScanInterval(2000)
                .setConnectTimeout(1000)
                .setTimeout(3000)
                .setRetryAttempts(3)
                .setRetryInterval(1500)
                ;

        prop.getNodes().forEach(node -> csc.addNodeAddress("redis://" + node));

        return Redisson.create(config);
    }
}

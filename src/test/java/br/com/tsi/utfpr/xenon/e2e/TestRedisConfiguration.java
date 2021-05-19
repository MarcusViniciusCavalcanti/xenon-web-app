package br.com.tsi.utfpr.xenon.e2e;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Profile("test")
@TestConfiguration
public class TestRedisConfiguration {
    private final RedisServer redisServer;

    public TestRedisConfiguration(@Value("${xenon.configurations.redis.port}") final Integer port) {
        this.redisServer = new RedisServer(port);
    }

    @PostConstruct
    public void postConstruct() {
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }
}

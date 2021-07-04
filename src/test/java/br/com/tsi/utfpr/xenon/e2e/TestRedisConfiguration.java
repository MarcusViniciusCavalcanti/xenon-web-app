package br.com.tsi.utfpr.xenon.e2e;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.util.SocketUtils;
import redis.embedded.RedisServer;

@Profile("test")
@TestConfiguration
public class TestRedisConfiguration {

    private final RedisServer redisServer;

    public TestRedisConfiguration(ConfigurableApplicationContext applicationContext) {
        var randomPort = SocketUtils.findAvailableTcpPort();
        redisServer = new RedisServer(randomPort);
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
            applicationContext, "xenon.configurations.redis.port=" + randomPort);
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

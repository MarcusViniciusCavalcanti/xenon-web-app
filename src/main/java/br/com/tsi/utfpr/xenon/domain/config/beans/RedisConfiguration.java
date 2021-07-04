package br.com.tsi.utfpr.xenon.domain.config.beans;

import br.com.tsi.utfpr.xenon.domain.config.property.RedisProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisConfiguration {

    private final RedisProperty xenonProperty;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        var redisStandalone = new RedisStandaloneConfiguration();

        redisStandalone.setHostName(xenonProperty.getHost());
        redisStandalone.setPort(xenonProperty.getPort());

        return new JedisConnectionFactory(redisStandalone);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        var template = new RedisTemplate<String, String>();
        var stringRedisSerializer = new StringRedisSerializer();

        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(stringRedisSerializer);

        return template;
    }
}

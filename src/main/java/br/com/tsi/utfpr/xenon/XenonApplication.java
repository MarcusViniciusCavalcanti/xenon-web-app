package br.com.tsi.utfpr.xenon;

import br.com.tsi.utfpr.xenon.domain.config.property.AvatarProperty;
import br.com.tsi.utfpr.xenon.domain.config.property.EmailProperty;
import br.com.tsi.utfpr.xenon.domain.config.property.RedisProperty;
import br.com.tsi.utfpr.xenon.domain.config.property.ThemeleafProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(
    exclude = {RedisAutoConfiguration.class}
)
@EnableConfigurationProperties(value = {
    AvatarProperty.class,
    RedisProperty.class,
    ThemeleafProperty.class,
    EmailProperty.class
})
public class XenonApplication {

    public static void main(String[] args) {
        SpringApplication.run(XenonApplication.class, args);
    }

}

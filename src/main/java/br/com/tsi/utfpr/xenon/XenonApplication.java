package br.com.tsi.utfpr.xenon;

import br.com.tsi.utfpr.xenon.domain.config.property.EmailProperty;
import br.com.tsi.utfpr.xenon.domain.config.property.FilesProperty;
import br.com.tsi.utfpr.xenon.domain.config.property.RedisProperty;
import br.com.tsi.utfpr.xenon.domain.config.property.ThymeleafProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(
    exclude = {RedisAutoConfiguration.class}
)
@EnableConfigurationProperties(value = {
    FilesProperty.class,
    RedisProperty.class,
    ThymeleafProperty.class,
    EmailProperty.class
})
public class XenonApplication {

    public static void main(String[] args) {
        SpringApplication.run(XenonApplication.class, args);
    }

}

package br.com.tsi.utfpr.xenon.domain.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("xenon.configurations.avatar")
public class AvatarProperty {

    private String url;
}

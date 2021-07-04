package br.com.tsi.utfpr.xenon.domain.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("xenon.configurations.thymeleaf")
public class ThymeleafProperty {

    private boolean cache = false;
}

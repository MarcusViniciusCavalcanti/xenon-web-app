package br.com.tsi.utfpr.xenon.domain.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "xenon.configurations.themeleaf")
public class ThemeleafProperty {
    private boolean cache = false;
}

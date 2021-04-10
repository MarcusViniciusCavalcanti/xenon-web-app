package br.com.tsi.utfpr.xenon.domain.config.application;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.decorators.strategies.GroupingStrategy;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.dialect.springdata.SpringDataDialect;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
@EnableAutoConfiguration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect(new GroupingStrategy());
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

}

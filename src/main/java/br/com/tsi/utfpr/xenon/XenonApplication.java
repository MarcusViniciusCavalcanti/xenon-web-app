package br.com.tsi.utfpr.xenon;

import br.com.tsi.utfpr.xenon.structure.config.property.XenonProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = {
    XenonProperty.class
})
@SpringBootApplication
public class XenonApplication {



    public static void main(String[] args) {
        SpringApplication.run(XenonApplication.class, args);
    }

}

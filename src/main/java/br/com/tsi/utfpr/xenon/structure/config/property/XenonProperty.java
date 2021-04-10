package br.com.tsi.utfpr.xenon.structure.config.property;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
@ConfigurationProperties(prefix = "xenon")
public class XenonProperty {

    @NestedConfigurationProperty
    private Configurations configurations;

    @Getter
    @Setter
    public class Configurations {

        private Themeleaf themeleaf;
        private Datasource datasource;
        private Avatar avatar;
    }

    @Getter
    @Setter
    public class Themeleaf {

        private boolean cache = false;
    }

    @Getter
    @Setter
    public class Datasource {

        @NotNull
        @NotBlank
        private String driver;

        @NotNull
        @NotBlank
        private String url;

        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    public class Avatar {

        private String url;
    }

}

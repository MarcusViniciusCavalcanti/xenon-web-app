package br.com.tsi.utfpr.xenon.domain.user.config.bean;

import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.service.GetterAllUserSpec;
import br.com.tsi.utfpr.xenon.domain.user.service.ParametersGetAllSpec;
import br.com.tsi.utfpr.xenon.structure.data.BasicSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpecificationConfiguration {

    public static final String QUALIFIER_GET_ALL_SPEC = "QUALIFIER_GET_ALL_SPEC";

    @Bean(QUALIFIER_GET_ALL_SPEC)
    public BasicSpecification<User, ParametersGetAllSpec> getterAllUserSpec() {
        return new GetterAllUserSpec();
    }
}

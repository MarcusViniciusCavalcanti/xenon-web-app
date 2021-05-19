package br.com.tsi.utfpr.xenon.application.config;

import br.com.tsi.utfpr.xenon.application.service.RegistryStudentsApplicationService;
import br.com.tsi.utfpr.xenon.application.service.UserApplicationService;
import br.com.tsi.utfpr.xenon.application.service.impl.RegistryStudentsApplicationServiceImpl;
import br.com.tsi.utfpr.xenon.application.service.impl.UserApplicationServiceImpl;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterPageUser;
import br.com.tsi.utfpr.xenon.domain.user.usecase.RegistryNewStudents;
import br.com.tsi.utfpr.xenon.domain.user.usecase.ValidateToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationServiceConfigurations {

    @Bean
    public UserApplicationService userApplicationService(GetterPageUser getterPageUser) {
        return new UserApplicationServiceImpl(getterPageUser);
    }

    @Bean
    public RegistryStudentsApplicationService registryStudentsApplicationService(
        RegistryNewStudents registryNewStudents,
        ValidateToken validateToken) {
        return new RegistryStudentsApplicationServiceImpl(registryNewStudents, validateToken);
    }
}

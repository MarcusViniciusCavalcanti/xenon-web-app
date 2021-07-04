package br.com.tsi.utfpr.xenon.application.config;

import br.com.tsi.utfpr.xenon.application.service.RegistryStudentsApplicationService;
import br.com.tsi.utfpr.xenon.application.service.UserApplicationService;
import br.com.tsi.utfpr.xenon.application.service.impl.RegistryStudentsApplicationServiceImpl;
import br.com.tsi.utfpr.xenon.application.service.impl.UserApplicationServiceImpl;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterCurrentUser;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterFiles;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterPageUser;
import br.com.tsi.utfpr.xenon.domain.user.usecase.RegistryNewStudents;
import br.com.tsi.utfpr.xenon.domain.user.usecase.RegistryUser;
import br.com.tsi.utfpr.xenon.domain.user.usecase.UpdateUser;
import br.com.tsi.utfpr.xenon.domain.user.usecase.ValidateToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationServiceConfigurations {

    @Bean
    public UserApplicationService userApplicationService(
        GetterPageUser getterPageUser,
        GetterFiles getterFiles,
        RegistryUser registryUser,
        UpdateUser updateUser,
        GetterCurrentUser getterCurrentUser) {

        return new UserApplicationServiceImpl(
            getterPageUser,
            getterFiles,
            registryUser,
            updateUser,
            getterCurrentUser
        );
    }

    @Bean
    public RegistryStudentsApplicationService registryStudentsApplicationService(
        RegistryNewStudents registryNewStudents, ValidateToken validateToken) {
        return new RegistryStudentsApplicationServiceImpl(registryNewStudents, validateToken);
    }
}

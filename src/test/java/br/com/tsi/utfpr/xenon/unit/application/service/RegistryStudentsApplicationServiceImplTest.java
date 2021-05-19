package br.com.tsi.utfpr.xenon.unit.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.tsi.utfpr.xenon.application.service.impl.RegistryStudentsApplicationServiceImpl;
import br.com.tsi.utfpr.xenon.domain.user.usecase.RegistryNewStudents;
import br.com.tsi.utfpr.xenon.domain.user.usecase.ValidateToken;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputNewStudent;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputValidateTokenDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - RegistryStudentsApplicationService")
class RegistryStudentsApplicationServiceImplTest {

    @Mock
    private RegistryNewStudents registryNewStudents;

    @Mock
    private ValidateToken validateToken;

    @InjectMocks
    private RegistryStudentsApplicationServiceImpl registryStudentsApplicationService;

    @Test
    @DisplayName("Deve incluir e-mail para cadastro")
    void shouldHaveIncludeNewStudents() {
        var email = "email@email.com";
        doNothing()
            .when(registryNewStudents)
            .includeNew(eq(email));

        registryStudentsApplicationService.includeNewRegistry(email);

        verify(registryNewStudents).includeNew(eq(email));
    }
    
    @Test
    @DisplayName("Deve validar token")
    void shouldHaveValidateToken() {
        var input = new InputValidateTokenDto();

        when(validateToken.validate(eq(input))).thenReturn("token");

        registryStudentsApplicationService.validateToken(input);

        verify(validateToken).validate(eq(input));
    }

    @Test
    @DisplayName("Deve registrar novo estudante com sucesso")
    void shouldHaveRegisterNewStudents() {
        var input = new InputNewStudent();

        doNothing()
            .when(registryNewStudents)
            .registry(eq(input));

        registryStudentsApplicationService.registryNewStudent(input);

        verify(registryNewStudents).registry(eq(input));
    }
}

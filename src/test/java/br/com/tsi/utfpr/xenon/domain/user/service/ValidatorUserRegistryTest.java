package br.com.tsi.utfpr.xenon.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.security.repository.AccessCardRepository;
import br.com.tsi.utfpr.xenon.domain.user.exception.UsernameException;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultCheckerDto.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - ValidatorUserRegistry")
class ValidatorUserRegistryTest {

    private static final String USERNAME_USERNAME_COM = "username@username.com";

    @Mock
    private AccessCardRepository accessCardRepository;

    @InjectMocks
    private ValidatorUserRegistry validatorUserRegistry;

    @Test
    @DisplayName("Deve Lançar Exceptio quando usuário já cadastrado")
    void shouldThrowsUsernameException() {
        when(accessCardRepository.exists(any())).thenReturn(Boolean.TRUE);

        var exception = assertThrows(UsernameException.class,
            () -> validatorUserRegistry.validateToToken(USERNAME_USERNAME_COM));

        var resultUsernameCheckerDto = exception.getResultUsernameCheckerDto();

        assertEquals(Result.USER_CAN_BE_REGISTERED,
            resultUsernameCheckerDto.getResult().getValue());
        assertEquals("Usuário já existe", resultUsernameCheckerDto.getResult().getReason());
        assertEquals(USERNAME_USERNAME_COM, resultUsernameCheckerDto.getUsername());
    }
}

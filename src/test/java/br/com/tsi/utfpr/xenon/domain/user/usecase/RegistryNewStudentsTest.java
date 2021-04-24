package br.com.tsi.utfpr.xenon.domain.user.usecase;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import br.com.tsi.utfpr.xenon.domain.user.aggregator.Token;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.TokenAdapter;
import br.com.tsi.utfpr.xenon.domain.user.service.ValidatorUserRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - RegistryNewStudents")
class RegistryNewStudentsTest {

    @Mock
    private ValidatorUserRegistry validatorUserRegistry;

    @Mock
    private TokenAdapter tokenAdapter;

    @InjectMocks
    private RegistryNewStudents registryNewStudents;

    @Test
    @DisplayName("Deve criar novo token com sucesso")
    void shouldHaveCreateNewToken() {
        var email = "email@email.com";
        doNothing()
            .when(validatorUserRegistry)
            .validateToToken(email);

        doNothing()
            .when(tokenAdapter)
            .saveToken(any(Token.class));

        registryNewStudents.includeNew(email);

        verify(validatorUserRegistry).validateToToken(email);
        verify(tokenAdapter).saveToken(any(Token.class));
    }
}

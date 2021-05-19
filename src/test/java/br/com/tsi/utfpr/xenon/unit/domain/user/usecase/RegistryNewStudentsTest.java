package br.com.tsi.utfpr.xenon.unit.domain.user.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import br.com.tsi.utfpr.xenon.domain.user.aggregator.EmailSenderAdapter;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.Token;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.TokenAdapter;
import br.com.tsi.utfpr.xenon.domain.user.exception.TokenException;
import br.com.tsi.utfpr.xenon.domain.user.exception.UsernameException;
import br.com.tsi.utfpr.xenon.domain.user.service.ValidatorUserRegistry;
import br.com.tsi.utfpr.xenon.domain.user.usecase.RegistryNewStudents;
import br.com.tsi.utfpr.xenon.domain.user.usecase.ValidateToken;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputNewStudent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - RegistryNewStudents")
class RegistryNewStudentsTest {

    public static final long MILLIS_300 = 300L;
    @Mock
    private ValidatorUserRegistry validatorUserRegistry;

    @Mock
    private TokenAdapter tokenAdapter;

    @Mock
    private EmailSenderAdapter emailSenderAdapter;

    @Mock
    private ValidateToken validateToken;

    @InjectMocks
    private RegistryNewStudents registryNewStudents;

    @Test
    @DisplayName("Deve lançar exception quando e-mail já cadastrado")
    void shouldThrowsExceptionWhenEmailExist() {
        var email = "testes@testes.com";
        doThrow(UsernameException.class)
            .when(validatorUserRegistry)
            .validateToInclude(email);

        assertThrows(UsernameException.class, () -> registryNewStudents.includeNew(email));

        verify(validatorUserRegistry).validateToInclude(email);
        verify(tokenAdapter, never()).saveToken(any(Token.class), anyLong());
    }

    @Test
    @DisplayName("Deve criar novo token com sucesso")
    void shouldHaveCreateNewToken() {
        var email = "email@email.com";
        doNothing()
            .when(validatorUserRegistry)
            .validateToInclude(email);

        doNothing()
            .when(tokenAdapter)
            .saveToken(any(Token.class), anyLong());

        registryNewStudents.includeNew(email);

        verify(validatorUserRegistry).validateToInclude(email);
        verify(tokenAdapter, timeout(MILLIS_300)).saveToken(any(Token.class), anyLong());
        verify(emailSenderAdapter, timeout(MILLIS_300)).sendMail(any(Token.class));
    }

    @Test
    @DisplayName("Deve Lançar exception quando não passar na validação [token]")
    void shouldThrowsExceptionWhenValidateNotPassedBecauseToken() {
        var input = createInput();

        doThrow(TokenException.class)
            .when(validateToken)
            .validateRegistry(eq(input.getEmail()), eq(input.getTokenRegistry()));

        assertThrows(TokenException.class, () -> registryNewStudents.registry(input));

        verify(validateToken).validateRegistry(eq(input.getEmail()), eq(input.getTokenRegistry()));
    }

    @Test
    @DisplayName("Deve Lançar exception quando não passar na validação [email]")
    void shouldThrowsExceptionWhenValidateNotPassedBecauseEmail() {
        var input = createInput();

        doNothing()
            .when(validateToken)
            .validateRegistry(eq(input.getEmail()), eq(input.getTokenRegistry()));

        doThrow(UsernameException.class)
            .when(validatorUserRegistry)
            .validateToRegistry(eq(input));

        assertThrows(UsernameException.class, () -> registryNewStudents.registry(input));

        verify(validateToken).validateRegistry(eq(input.getEmail()), eq(input.getTokenRegistry()));
        verify(validatorUserRegistry).validateToRegistry(eq(input));
    }

    private InputNewStudent createInput() {
        var input = new InputNewStudent();
        input.setEmail("email");
        input.setPlateCar("plate");
        input.setTokenRegistry("token");
        input.setPassword("pass");
        input.setConfirmPassword("pass");
        input.setName("name");
        input.setTokenRegistry("token");
        return input;
    }

}

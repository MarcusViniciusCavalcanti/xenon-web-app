package br.com.tsi.utfpr.xenon.unit.domain.user.usecase;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.user.aggregator.TokenAdapter;
import br.com.tsi.utfpr.xenon.domain.user.exception.TokenException;
import br.com.tsi.utfpr.xenon.domain.user.usecase.ValidateToken;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputValidateTokenDto;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Test - Unidade - ValidateToken")
@ExtendWith(MockitoExtension.class)
class ValidateTokenTest {

    private static final String EMAIL_EMAIL_COM = "email@email.com";

    @Mock
    private TokenAdapter tokenAdapter;

    @InjectMocks
    private ValidateToken validateToken;

    @Test
    @DisplayName("Deve lançar exception quando token não corresponde")
    void shouldThrowsTokenException() {
        var input = new InputValidateTokenDto();
        input.setEmail(EMAIL_EMAIL_COM);
        input.setToken(UUID.randomUUID().toString());

        when(tokenAdapter.getToken(eq(input.getEmail())))
            .thenReturn(Optional.of(UUID.randomUUID().toString()));

        assertThrows(TokenException.class, () -> validateToken.validate(input));

        verify(tokenAdapter).getToken(eq(input.getEmail()));
        verify(tokenAdapter, never()).deleteToken(any());
    }

    @Test
    @DisplayName("Deve lançar exception quando token não encontrado")
    void shouldThrowsTokenExceptionWhenTokenNotFound() {
        var input = new InputValidateTokenDto();
        input.setEmail(EMAIL_EMAIL_COM);
        input.setToken(UUID.randomUUID().toString());

        when(tokenAdapter.getToken(eq(input.getEmail()))).thenReturn(Optional.empty());

        assertThrows(TokenException.class, () -> validateToken.validate(input));

        verify(tokenAdapter).getToken(eq(input.getEmail()));
        verify(tokenAdapter, never()).deleteToken(any());
    }

    @Test
    @DisplayName("Deve validar token com sucesso quando corresponde")
    void shouldThrowsSuccessfully() {
        var token = UUID.randomUUID().toString();

        var input = new InputValidateTokenDto();
        input.setEmail(EMAIL_EMAIL_COM);
        input.setToken(token);

        when(tokenAdapter.getToken(eq(input.getEmail()))).thenReturn(Optional.of(token));
        when(tokenAdapter.deleteToken(eq(input.getEmail()))).thenReturn(Boolean.TRUE);

        validateToken.validate(input);

        verify(tokenAdapter).getToken(eq(input.getEmail()));
        verify(tokenAdapter).deleteToken(eq(input.getEmail()));
    }

    @Test
    @DisplayName("Deve lançar exception quando token de registro não encontrado")
    void shouldThrowsExceptionWhenTokenRegisterNotFound() {

        when(tokenAdapter.getToken(any())).thenReturn(Optional.empty());

        assertThrows(TokenException.class,
            () -> validateToken.validateRegistry("email@email", "token"));

        verify(tokenAdapter, never()).deleteToken(any());
    }

    @Test
    @DisplayName("Deve validar token para registro com sucesso")
    void shouldHaveValidateTokenToRegistry() {
        var token = UUID.randomUUID();
        var email = "email@email.com";

        var key = String.format("%s - %s", email, UUID.nameUUIDFromBytes(email.getBytes(UTF_8)));

        when(tokenAdapter.getToken(eq(key))).thenReturn(Optional.of(token.toString()));
        when(tokenAdapter.deleteToken(eq(key))).thenReturn(Boolean.TRUE);

        validateToken.validateRegistry(email, token.toString());

        verify(tokenAdapter).getToken(eq(key));
        verify(tokenAdapter).deleteToken(eq(key));
    }
}

package br.com.tsi.utfpr.xenon.unit.domain.user.aggregator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.tsi.utfpr.xenon.domain.user.aggregator.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Test - Unidade - Token")
class TokenTest {

    @ParameterizedTest
    @ValueSource(strings = { "", "    "})
    @DisplayName("Deve lançar exception quando estado da aplicação está inválido - e-mail vazio")
    void shouldThrowsExceptionWhenEmailIsEmpty(String value) {
        var token = new Token(value);

        var exception = assertThrows(IllegalStateException.class, token::generateNewToken);
        assertEquals("E-mail está vázio, verifique o preenchimento", exception.getMessage());
    }

    @Test
    @DisplayName("Deve gerar token com sucesso")
    void shouldHaveCreateToken() {
        var token = new Token("email@email.com");

        token.generateNewToken();
        var tokenValue = token.getToken();

        assertNotNull(tokenValue);
        assertTrue(tokenValue.matches("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"));
    }
}

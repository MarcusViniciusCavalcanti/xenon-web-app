package br.com.tsi.utfpr.xenon.unit.domain.user.aggregator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.user.aggregator.Token;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.TokenAdapter;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@DisplayName("Test - Unidade - TokenAdapter")
@ExtendWith(MockitoExtension.class)
class TokenAdapterTest {

    @Mock
    private ValueOperations<String, String> valueOperation;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private TokenAdapter tokenAdapter;

    @Test
    @DisplayName("Deve salvar token")
    void shouldHaveSaveToken() {
        var token = new Token("email@email.com");
        token.generateNewToken();

        when(redisTemplate.opsForValue()).thenReturn(valueOperation);
        doNothing()
            .when(valueOperation)
            .set(token.getKey(), token.getToken(), 5, TimeUnit.MINUTES);

        tokenAdapter.saveToken(token, 5);

        verify(redisTemplate).opsForValue();
        verify(valueOperation).set(token.getKey(), token.getToken(), 5, TimeUnit.MINUTES);
    }

    @Test
    @DisplayName("Deve recuprear token")
    void shouldReturnToken() {
        var email = "email@email.com";
        var tokenValue = "token";

        when(redisTemplate.opsForValue()).thenReturn(valueOperation);
        when(valueOperation.get(any())).thenReturn(tokenValue);

        var token = tokenAdapter.getToken(email).get();

        assertEquals(tokenValue, token);
        verify(valueOperation).get(email);
    }

    @Test
    @DisplayName("Deve deletar token")
    void shouldHaveDeleteToken() {
        var email = "email@email.com";

        when(redisTemplate.delete(email)).thenReturn(Boolean.TRUE);

        assertTrue(tokenAdapter.deleteToken(email));

        verify(redisTemplate).delete(email);
    }
}

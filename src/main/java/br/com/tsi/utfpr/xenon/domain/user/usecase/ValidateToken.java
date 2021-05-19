package br.com.tsi.utfpr.xenon.domain.user.usecase;

import static java.nio.charset.StandardCharsets.UTF_8;

import br.com.tsi.utfpr.xenon.domain.user.aggregator.Token;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.TokenAdapter;
import br.com.tsi.utfpr.xenon.domain.user.exception.TokenException;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputValidateTokenDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ValidateToken {

    private static final String FORMAT = "%s - %s";
    private static final int TIME_EXPIRED = 60;
    private final TokenAdapter tokenAdapter;

    public String validate(InputValidateTokenDto inputToken) {
        var token = tokenAdapter.getToken(inputToken.getEmail())
            .orElseThrow(() -> new TokenException(inputToken.getToken()));
        var expected = UUID.fromString(token);
        var actual = UUID.fromString(inputToken.getToken());

        checkToken(expected, actual);

        tokenAdapter.deleteToken(inputToken.getEmail());
        var key = createKey(inputToken.getEmail());
        var tokenRegistry = new Token(key);
        tokenRegistry.generateNewToken();
        tokenAdapter.saveToken(tokenRegistry, TIME_EXPIRED);

        return tokenRegistry.getToken();
    }

    public void validateRegistry(String email, String expected) {
        final var key = createKey(email);
        var actual = tokenAdapter.getToken(key)
            .orElseThrow(() -> new TokenException(expected));

        checkToken(UUID.fromString(expected), UUID.fromString(actual));
        tokenAdapter.deleteToken(key);
    }

    private String createKey(String email) {
        var uuid = UUID.nameUUIDFromBytes(email.getBytes(UTF_8));
        return String.format(FORMAT, email, uuid);
    }

    private void checkToken(UUID expected, UUID actual) {
        if (expected.compareTo(actual) != 0) {
            throw new TokenException(actual.toString());
        }
    }
}

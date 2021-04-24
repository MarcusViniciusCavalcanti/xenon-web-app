package br.com.tsi.utfpr.xenon.domain.user.aggregator;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class Token {

    private final String email;
    private String token;

    public Token(String email) {
        this.email = email;
    }

    public void generateNewToken() {
        token = UUID.nameUUIDFromBytes(email.getBytes(StandardCharsets.UTF_8)).toString();
    }
}

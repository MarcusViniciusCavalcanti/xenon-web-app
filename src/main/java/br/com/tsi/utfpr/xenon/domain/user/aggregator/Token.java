package br.com.tsi.utfpr.xenon.domain.user.aggregator;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public final class Token {

    private final String key;
    private String token;

    public Token(String email) {
        this.key = email;
    }

    public void generateNewToken() {
        Assert.state(isNotBlank(key), "E-mail está vázio, verifique o preenchimento");
        var params = String.format("%s%d", key, System.currentTimeMillis());
        token = UUID.nameUUIDFromBytes(params.getBytes(StandardCharsets.UTF_8)).toString();
    }
}

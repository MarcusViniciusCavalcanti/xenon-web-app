package br.com.tsi.utfpr.xenon.domain.user.aggregator;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenAdapter {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveToken(Token token, long timeExpired) {
        redisTemplate.opsForValue()
            .set(token.getKey(), token.getToken(), timeExpired, TimeUnit.MINUTES);
    }

    public Optional<String> getToken(String email) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(email));
    }

    public Boolean deleteToken(String email) {
        return redisTemplate.delete(email);
    }
}

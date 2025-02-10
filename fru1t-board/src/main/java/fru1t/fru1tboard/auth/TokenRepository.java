package fru1t.fru1tboard.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class TokenRepository {
    private final StringRedisTemplate redisTemplate;

    // auth:token:refresh:%s
    private static final String KEY_FORMAT = "auth:token:refresh:%s";
    private static final long REFRESH_TOKEN_TTL = 7;
    public void saveRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue().set(generateKey(username), refreshToken, REFRESH_TOKEN_TTL, TimeUnit.DAYS);
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(generateKey(username));
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete(generateKey(username));
    }
    private String generateKey(String username) {
        return String.format(KEY_FORMAT, username);
    }
}
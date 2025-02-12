package fru1t.fru1tboard.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;
    private Key secretKey;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        try {
            Field secretKeyField = JwtUtil.class.getDeclaredField("SECRET_KEY");
            secretKeyField.setAccessible(true);
            secretKey = (Key) secretKeyField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to access SECRET_KEY in JwtUtil", e);
        }
    }

    private String generateToken(long expirationMillis, Key key, SignatureAlgorithm algorithm) {
        return Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key, algorithm)
                .compact();
    }

    @Test
    @DisplayName("유효한 JWT 토큰을 검증하면 true를 반환해야 한다.")
    void validateToken_ShouldReturnTrue_WhenValidToken() {
        String token = generateToken(60000, secretKey, SignatureAlgorithm.HS256);
        assertTrue(jwtUtil.validateToken(token), "유효한 토큰이어야 합니다.");
    }

    @Test
    @DisplayName("만료된 토큰을 검증하면 예외가 발생해야 한다.")
    void validateToken_ShouldThrowException_WhenTokenExpired() {
        String token = generateToken(-1000, secretKey, SignatureAlgorithm.HS256);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(token));
        assertEquals("Token expired", thrown.getMessage());
    }

    @Test
    @DisplayName("토큰이 변조되었을 경우 예외가 발생해야 한다.")
    void validateToken_ShouldThrowException_WhenTokenTampered() {
        String token = generateToken(60000, secretKey, SignatureAlgorithm.HS256) + "tampered";
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(token));
        assertEquals("Invalid JWT signature", thrown.getMessage());
    }

    @Test
    @DisplayName("잘못된 형식의 토큰이 주어지면 예외가 발생해야 한다.")
    void validateToken_ShouldThrowException_WhenTokenMalformed() {
        String token = "thisIsNotAValidJwtToken";
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(token));
        assertEquals("Invalid JWT token", thrown.getMessage());
    }

    @Test
    @DisplayName("잘못된 알고리즘(HMAC 대신 다른 알고리즘)으로 서명된 토큰을 검증하면 예외가 발생해야 한다.")
    void validateToken_ShouldThrowException_WhenAlgorithmInvalid() {
        // Given
        byte[] keyBytes = new byte[64];
        new java.security.SecureRandom().nextBytes(keyBytes);
        Key differentKey = Keys.hmacShaKeyFor(keyBytes);

        String token = generateToken(60000, differentKey, SignatureAlgorithm.HS512);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(token));

        assertTrue(
                thrown.getMessage().contains("Invalid JWT signature") ||
                        thrown.getMessage().contains("JWT processing error")
        );
    }

    @Test
    @DisplayName("AccessToken을 정상적으로 생성해야 한다.")
    void generateAccessTokenTest() {
        String username = "test";
        String accessToken = jwtUtil.generateAccessToken(username);
        assertNotNull(accessToken, "Access Token이 null이 아니어야 합니다.");
        assertFalse(accessToken.isEmpty(), "Access Token이 비어 있으면 안 됩니다.");
        assertTrue(jwtUtil.validateToken(accessToken));
    }

    @Test
    @DisplayName("알고리즘을 none으로 설정한 토큰이 주어지면 예외가 발생해야 한다.")
    void validateToken_ShouldThrowException_WhenAlgorithmNoneUsed() {
        String noneAlgorithmToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .compact(); // 서명 없음

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(noneAlgorithmToken));
        assertTrue(thrown.getMessage().contains("JWT processing error"), "서명이 없는 JWT는 예외를 발생해야 합니다.");
    }
}

package fru1t.fru1tboard.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;
    private Key secretKey;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        secretKey = Keys.hmacShaKeyFor(
                "kR7vXP3YaUqM5z9B2sJpXQ8nLrYeTd4VkR7vXP3YaUqM5z9B2sJpXQ8nLrYeTd4V".getBytes()
        );
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
        // Given
        String token = generateToken(60000, secretKey, SignatureAlgorithm.HS256);

        // When
        boolean isValid = jwtUtil.validateToken(token);

        // Then
        assertTrue(isValid, "유효한 토큰이어야 합니다.");
    }

    @Test
    @DisplayName("만료된 토큰을 검증하면 예외가 발생해야 한다.")
    void validateToken_ShouldThrowException_WhenTokenExpired() {
        // Given
        String token = generateToken(-1000, secretKey, SignatureAlgorithm.HS256);

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(token));
        assertEquals("Token expired", thrown.getMessage());
    }

    @Test
    @DisplayName("토큰이 변조되었을 경우 예외가 발생해야 한다.")
    void validateToken_ShouldThrowException_WhenTokenTampered() {
        // Given
        String token = generateToken(60000, secretKey, SignatureAlgorithm.HS256) + "tampered";

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(token));
        assertEquals("Invalid JWT signature", thrown.getMessage());
    }

    @Test
    @DisplayName("잘못된 형식의 토큰이 주어지면 예외가 발생해야 한다.")
    void validateToken_ShouldThrowException_WhenTokenMalformed() {
        // Given
        String token = "thisIsNotAValidJwtToken";

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(token));
        assertEquals("Invalid JWT token", thrown.getMessage());
    }

    @Test
    @DisplayName("잘못된 알고리즘(HMAC 대신 다른 알고리즘)으로 서명된 토큰을 검증하면 예외가 발생해야 한다.")
    void validateToken_ShouldThrowException_WhenAlgorithmInvalid() {
        // Given
        Key differentKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String token = generateToken(60000, differentKey, SignatureAlgorithm.HS512);

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(token));
        assertEquals("Invalid JWT signature", thrown.getMessage());
    }

    @Test
    @DisplayName("AccessToken을 정상적으로 생성해야 한다.")
    void generateAccessTokenTest() {
        // Given
        String username = "test";

        // When
        String accessToken = jwtUtil.generateAccessToken(username);

        // Then
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
                .compact();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(noneAlgorithmToken));
        assertEquals("JWT processing error: Unsigned Claims JWTs are not supported.", thrown.getMessage());
    }

}

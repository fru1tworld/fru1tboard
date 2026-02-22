package fru1t.fru1tboard.auth;

import io.jsonwebtoken.*;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;

public class JwtUtil {
    private static final String SECRET_KEY_STRING = "kR7vXP3YaUqM5z9B2sJpXQ8nLrYeTd4VkR7vXP3YaUqM5z9B2sJpXQ8nLrYeTd4V"; // 32바이트 Base64 인코딩된 키 필요
    private static final Key SECRET_KEY;

    static {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY_STRING);
            SECRET_KEY = new SecretKeySpec(keyBytes, "HmacSHA256");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Base64 SECRET_KEY_STRING. Ensure it's a valid 32-byte Base64 encoded key.", e);
        }
    }

    private static final long ACCESS_TOKEN_EXPIRY = 60 * 60 * 1000; // 1시간
    private static final long REFRESH_TOKEN_EXPIRY = 7 * 24 * 60 * 60 * 1000; // 7일

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
                .claim("tokenType", "access")
                .signWith(SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
                .claim("tokenType", "refresh")
                .signWith(SECRET_KEY)
                .compact();
    }

    public boolean validateTokenByType(String token, String tokenTypeName) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);

            String tokenType = claimsJws.getBody().get("tokenType", String.class);
            if (tokenTypeName.equals(tokenType)) {
                return true;
            } else {
                throw new RuntimeException("Invalid token type");
            }
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expired");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid JWT token");
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid JWT signature");
        } catch (JwtException e) {
            throw new RuntimeException("JWT processing error: " + e.getMessage());
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

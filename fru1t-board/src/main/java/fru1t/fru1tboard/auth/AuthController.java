package fru1t.fru1tboard.auth;

import fru1t.fru1tboard.auth.dto.AccessToken;
import fru1t.fru1tboard.auth.dto.LoginRequest;
import fru1t.fru1tboard.auth.dto.RefreshRequest;
import fru1t.fru1tboard.auth.dto.TokenResponse;
import fru1t.fru1tboard.config.CookieProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final CookieProperties cookieProperties;

    @PostMapping("/v1/login")
    public ResponseEntity<AccessToken> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        log.info("Login attempt with username: {}", request.getUsername());

        TokenResponse tokenResponse = authService.login(request);

        log.info("Login successful for username: {}", request.getUsername());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .sameSite("Strict")
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
                .build();

        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        log.info("Refresh token cookie set for username: {}", request.getUsername());

        return ResponseEntity.ok(AccessToken.create(tokenResponse.getAccessToken()));
    }

    @PostMapping("/v1/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        String accessToken = token.replace("Bearer ", "");
        log.debug("Logout attempt with access token: {}", accessToken);
        authService.logout(accessToken);

        log.debug("Logout successful for token: {}", accessToken);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/v1/refresh")
    public ResponseEntity<AccessToken> refreshToken(@RequestHeader("Authorization") RefreshRequest request) {
        log.debug("Refresh token request received with refresh token: {}", request.getRefreshToken());

        TokenResponse tokenResponse = authService.refreshToken(request);

        log.debug("Token refresh successful, new access token generated.");

        return ResponseEntity.ok(AccessToken.create(tokenResponse.getAccessToken()));
    }
}

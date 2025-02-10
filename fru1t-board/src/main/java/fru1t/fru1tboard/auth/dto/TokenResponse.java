package fru1t.fru1tboard.auth.dto;

import lombok.Getter;

@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;

    private TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    private TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static TokenResponse withBothTokens(String accessToken, String refreshToken) {
        return new TokenResponse(accessToken, refreshToken);
    }

    public static TokenResponse withAccessTokenOnly(String accessToken) {
        return new TokenResponse(accessToken);
    }
}

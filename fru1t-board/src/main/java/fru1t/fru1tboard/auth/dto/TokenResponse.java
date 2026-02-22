package fru1t.fru1tboard.auth.dto;

import lombok.Getter;

@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;

    public static TokenResponse withBothTokens(String accessToken, String refreshToken) {
        TokenResponse response = new TokenResponse();
        response.accessToken = accessToken;
        response.refreshToken = refreshToken;
        return response;
    }
    public static TokenResponse withAccessTokenOnly(String accessToken) {
        TokenResponse response = new TokenResponse();
        response.accessToken = accessToken;
        return response;
    }
}

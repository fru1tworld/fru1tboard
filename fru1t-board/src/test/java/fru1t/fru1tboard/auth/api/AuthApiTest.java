package fru1t.fru1tboard.auth.api;

import fru1t.fru1tboard.auth.dto.LoginRequest;
import fru1t.fru1tboard.auth.dto.RefreshRequest;
import fru1t.fru1tboard.auth.dto.TokenResponse;
import fru1t.fru1tboard.user.request.SignUpForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Transactional
public class AuthApiTest {
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password123";

    private final RestClient restClient = RestClient.create("https://localhost:8080");

    @BeforeEach
    void setUp() {
        signUpForTest();
    }

    @AfterEach
    void tearDown() {
        deleteUserForTest();
    }

    @Test
    void loginTest(){
        LoginRequest request = new LoginRequest(TEST_USERNAME, TEST_PASSWORD);

        TokenResponse response = login(request);
        System.out.println("Access Token = " + response.getAccessToken());
        System.out.println("Refresh Token = " + response.getRefreshToken());
    }

    @Test
    void logoutTest(){
        String token = "valid.jwt.token";

        logout(token);
        System.out.println("Logout successful for token: " + token);
    }

    @Test
    void refreshTokenTest(){
        RefreshRequest request = new RefreshRequest("validRefreshToken", TEST_USERNAME);

        TokenResponse response = refreshToken(request);
        System.out.println("New Access Token = " + response.getAccessToken());
    }

    void logout(String token) {
        restClient.post()
                .uri("/api/v1/logout")
                .body(token)
                .retrieve()
                .toBodilessEntity();
    }

    TokenResponse login(LoginRequest request){
        return restClient.post()
                .uri("/api/v1/login")
                .body(request)
                .retrieve()
                .body(TokenResponse.class);
    }

    TokenResponse refreshToken(RefreshRequest request){
        return restClient.post()
                .uri("/api/v1/refresh")
                .body(request)
                .retrieve()
                .body(TokenResponse.class);
    }

    void signUpForTest() {
        SignUpForm signUpForm = new SignUpForm(TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD);
        restClient.post()
                .uri("/api/v1/signup")
                .body(signUpForm)
                .retrieve()
                .toBodilessEntity();

        System.out.println("User created: " + TEST_USERNAME);
    }

    void deleteUserForTest() {
        restClient.delete()
                .uri("/api/v1/users/" + TEST_USERNAME)
                .retrieve()
                .toBodilessEntity();

        System.out.println("User deleted: " + TEST_USERNAME);
    }


    @Getter
    @AllArgsConstructor
    static class LoginRequest {
        private String username;
        private String password;
    }

    @Getter
    @AllArgsConstructor
    static class RefreshRequest {
        private String refreshToken;
        private String username;
    }


    @Getter
    @AllArgsConstructor
    static class SignUpForm{
        private String email;
        private String username;
        private String password;
    }
}

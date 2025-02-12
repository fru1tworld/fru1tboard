package fru1t.fru1tboard.auth.api;

import fru1t.fru1tboard.auth.dto.AccessToken;
import fru1t.fru1tboard.auth.dto.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestClient;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthApiTest {
    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password123";

    private final RestClient restClient = RestClient.create();

    @Test
    void testLogin() {
        LoginRequest loginRequest = new LoginRequest(TEST_USERNAME, TEST_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<AccessToken> response = restClient.post()
                .uri(BASE_URL + "/login")
                .body(request.getBody())
                .retrieve()
                .toEntity(AccessToken.class);


        System.out.println("Response Body: " + response.getBody());

        AccessToken accessToken = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(accessToken).isNotNull();
        assertThat(accessToken.getAccessToken()).isNotBlank();
    }


    @Test
    void testLogout() {
        LoginRequest loginRequest = new LoginRequest(TEST_USERNAME, TEST_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<AccessToken> response = restClient.post()
                .uri(BASE_URL + "/login")
                .body(request.getBody())
                .retrieve()
                .toEntity(AccessToken.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        String accessToken = response.getBody().getAccessToken();
        assertThat(accessToken).isNotBlank();

        ResponseEntity<Void> logoutResponse = restClient.post()
                .uri(BASE_URL + "/logout")
                .header("Authorization",  accessToken)
                .retrieve()
                .toEntity(Void.class);

        assertThat(logoutResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void refreshTokenTest(){
        LoginRequest loginRequest = new LoginRequest(TEST_USERNAME, TEST_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<AccessToken> response = restClient.post()
                .uri(BASE_URL + "/login")
                .body(request.getBody())
                .retrieve()
                .toEntity(AccessToken.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        String accessToken = response.getBody().getAccessToken();
        assertThat(accessToken).isNotBlank();
    }

}

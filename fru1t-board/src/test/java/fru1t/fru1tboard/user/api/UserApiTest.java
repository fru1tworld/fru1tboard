package fru1t.fru1tboard.user.api;

import fru1t.fru1tboard.user.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class UserApiTest {
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password123";

    private final RestClient restClient = RestClient.create();

    @Test
    void signUpTest(){
        SignUpForm signUpForm = new SignUpForm(TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD);
        UserResponse response = signUpForTest(signUpForm);
        System.out.println("response = " + response.getUsername());
    }

    UserResponse signUpForTest(SignUpForm signUpForm) {
        return restClient.post()
                .uri("/api/v1/signup")
                .body(signUpForm)
                .retrieve()
                .body(UserResponse.class);
    }

    void deleteUserForTest() {
        restClient.delete()
                .uri("/api/v1/users" + TEST_USERNAME)
                .retrieve()
                .toBodilessEntity();

        System.out.println("User deleted: " + TEST_USERNAME);
    }

    @Getter
    @AllArgsConstructor
    static class SignUpForm{
        private String email;
        private String username;
        private String password;
    }
}

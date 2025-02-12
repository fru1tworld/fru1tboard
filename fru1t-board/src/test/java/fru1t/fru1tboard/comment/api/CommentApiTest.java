package fru1t.fru1tboard.comment.api;


import fru1t.fru1tboard.auth.dto.AccessToken;
import fru1t.fru1tboard.auth.dto.LoginRequest;
import fru1t.fru1tboard.comment.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class CommentApiTest {
    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password123";
    private final RestClient restClient = RestClient.create("http://localhost:8080");
    private String accessToken;

    @BeforeEach
    void setUp() {
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

        accessToken = response.getBody().getAccessToken();
    }
    @Test
    void createTest(){
        CommentCreateRequest request = new CommentCreateRequest("my Comment",1L,null, 1L);

        CommentResponse response = create(request);
        System.out.println("response.getId = " + response.getCommentId());
    }

    @Test
    void readTest(){
        CommentResponse response = read(120115454734336L);
        System.out.println("response.content = " + response.getContent());
    }

    @Test
    void updateTest(){
        CommentUpdateRequest request = new CommentUpdateRequest("updated comment");
        CommentResponse response = update(120115454734336L, request);
        System.out.println("response.content = " + response.getContent());
    }
    @Test
    void deleteTest() {
        restClient.delete()
                .uri(BASE_URL + "/comments/{commentsId}", 120115454734336L)
                .header("Authorization",  accessToken)
                .retrieve()
                .toBodilessEntity();
    }
    CommentResponse update(Long commentId, CommentUpdateRequest request){
        return restClient.put()
                .uri(BASE_URL + "/comments/{commentId}",commentId)
                .header("Authorization",  accessToken)
                .body(request)
                .retrieve()
                .body(CommentResponse.class);

    }
    CommentResponse create(CommentCreateRequest request){
        return restClient.post()
                .uri(BASE_URL + "/api/v1/comments/")
                .header("Authorization",  accessToken)
                .body(request)
                .retrieve()
                .body(CommentResponse.class);

    }

    CommentResponse read(Long commentId){
        return restClient.get()
                .uri("/api/v1/comments/{commentsId}",commentId)
                .retrieve()
                .body(CommentResponse.class);

    }

    @Getter
    @AllArgsConstructor
    static class CommentCreateRequest{
        private String content;
        private Long articleId;
        private Long parentCommentId;
        private Long writerId;
    }

    @Getter
    @AllArgsConstructor
    static class CommentUpdateRequest{
        private String content;
    }
}

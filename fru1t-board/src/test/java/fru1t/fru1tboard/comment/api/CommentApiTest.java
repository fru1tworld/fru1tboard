package fru1t.fru1tboard.comment.api;


import fru1t.fru1tboard.auth.dto.AccessToken;
import fru1t.fru1tboard.auth.dto.LoginRequest;
import fru1t.fru1tboard.comment.response.CommentPageResponse;
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
    private final RestClient restClient = RestClient.builder()
            .baseUrl(BASE_URL)
            .build();
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

    @Test
    void readAllTest(){
//        CommentPageResponse commentPageResponse = readAllFirstPage(2L, 300L);
        CommentPageResponse commentPageResponse = readAllOtherPage(2L, 300L, 2321810714060453L);
        System.out.println("commentPageResponse = " + commentPageResponse.getCommentResponse().size());
        for(CommentResponse commentResponse : commentPageResponse.getCommentResponse()){
            System.out.println("commentResponse = " + commentResponse.getCommentId());
        }
    }

    CommentResponse update(Long commentId, CommentUpdateRequest request){
        return restClient.put()
                .uri( "/comments/{commentId}",commentId)
                .header("Authorization",  accessToken)
                .body(request)
                .retrieve()
                .body(CommentResponse.class);

    }
    CommentResponse create(CommentCreateRequest request){
        return restClient.post()
                .uri( "/comments/")
                .header("Authorization",  accessToken)
                .body(request)
                .retrieve()
                .body(CommentResponse.class);

    }

    CommentResponse read(Long commentId){
        return restClient.get()
                .uri( "/comments/{commentsId}",commentId)
                .retrieve()
                .body(CommentResponse.class);

    }

    CommentPageResponse readAllFirstPage(Long articleId, Long pageSize){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path( "/articles/{articleId}/comments")
                        .queryParam("pageSize", pageSize)
                        .build(articleId))
                .retrieve()
                .body(CommentPageResponse.class);
    }
    CommentPageResponse readAllOtherPage(Long articleId, Long pageSize, Long lastCommentId){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path( "/articles/{articleId}/comments")
                        .queryParam("pageSize", pageSize)
                        .queryParam("lastCommentId", lastCommentId)
                        .build(articleId))
                .retrieve()
                .body(CommentPageResponse.class);
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
    @Getter
    @AllArgsConstructor
    static class CommentPageRequest{
        private Long pageSize;
        private Long lastArticleId;

    }
}

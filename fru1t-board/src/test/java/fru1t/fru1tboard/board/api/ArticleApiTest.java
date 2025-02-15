package fru1t.fru1tboard.board.api;

import fru1t.fru1tboard.auth.dto.AccessToken;
import fru1t.fru1tboard.auth.dto.LoginRequest;
import fru1t.fru1tboard.board.response.ArticlePageResponse;
import fru1t.fru1tboard.board.response.ArticleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;


public class ArticleApiTest {
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
        ArticleCreateRequest request = new ArticleCreateRequest("my title", "my content", 1L, 1L);

        ArticleResponse response = create(request);
        System.out.println("response.getId = " + response.getArticleId());
    }


    @Test
    void readTest(){
        ArticleResponse response = read(120115454734336L);
        System.out.println("response.title = " + response.getTitle());
        System.out.println("response.content = " + response.getContent());
    }

    @Test
    void updateTest(){
        ArticleUpdateRequest request = new ArticleUpdateRequest("updated2 title", "updated content");
        ArticleResponse response = update(120115454734336L, request);
        System.out.println("response.title = " + response.getTitle());
        System.out.println("response.content = " + response.getContent());
    }
    @Test
    void deleteTest() {
            restClient.delete()
                    .uri("/articles/{articleId}", 120115454734336L)
                    .retrieve()
                    .toBodilessEntity();
    }

    @Test
    void readAllTest(){
//        ArticlePageResponse response = readAllFirstPage(300L);
//        ArticlePageResponse response = readAllOtherPage(300L, 2316383232737375L);
//        ArticlePageResponse response = readAllFirstPageByBoardId(2L, 300L);
        ArticlePageResponse response = readAllOtherPageByBoardId(2L, 300L, 2321872758784940L);

        System.out.println("response = " + response.getArticleResponses().size());
        for (ArticleResponse articleResponse : response.getArticleResponses()) {
            System.out.println("articleResponse = " + articleResponse.getArticleId());
        }
    }
    ArticleResponse update(Long articleId, ArticleUpdateRequest request){
        return restClient.put()
                .uri( "/articles/{articleId}",articleId)
                .body(request)
                .header("Authorization",  accessToken)
                .retrieve()
                .body(ArticleResponse.class);

    }
    ArticleResponse create(ArticleCreateRequest request){
        return restClient.post()
                .uri("/articles")
                .body(request)
                .header("Authorization",  accessToken)
                .retrieve()
                .body(ArticleResponse.class);

    }
    ArticleResponse read(Long articleId){
        return restClient.get()
                .uri("/articles/{articleId}",articleId)
                .retrieve()
                .body(ArticleResponse.class);

    }

    ArticlePageResponse readAllFirstPage(Long pageSize){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path( "/board/all/articles")
                        .queryParam("pageSize", pageSize)
                        .build())
                .retrieve()
                .body(ArticlePageResponse.class);
    }
    ArticlePageResponse readAllOtherPage(Long pageSize, Long lastArticleId){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path( "/board/all/articles")
                        .queryParam("pageSize", pageSize)
                        .queryParam("lastArticleId", lastArticleId)
                        .build())
                .retrieve()
                .body(ArticlePageResponse.class);
    }
    ArticlePageResponse readAllFirstPageByBoardId(Long boardId, Long pageSize){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path( "/board/{boardId}/articles")
                        .queryParam("pageSize", pageSize)
                        .build(boardId))
                .retrieve()
                .body(ArticlePageResponse.class);
    }
    ArticlePageResponse readAllOtherPageByBoardId(Long boardId, Long pageSize, Long lastArticleId){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path( "/board/{boardId}/articles")
                        .queryParam("pageSize", pageSize)
                        .queryParam("lastArticleId", lastArticleId)
                        .build(boardId))
                .retrieve()
                .body(ArticlePageResponse.class);
    }

    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest{
        private String title;
        private String content;
        private Long boardId;
        private Long writerId;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest{
        private String title;
        private String content;
    }
}

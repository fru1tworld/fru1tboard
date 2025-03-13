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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ArticleApiTest {
    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password123";
    private final RestClient restClient = RestClient.builder()
            .baseUrl(BASE_URL)
            .build();
    private String accessToken;
//    @BeforeEach
//    void setUp() {
//        LoginRequest loginRequest = new LoginRequest(TEST_USERNAME, TEST_PASSWORD);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(MediaType.parseMediaTypes("application/json"));
//        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
//
//        ResponseEntity<AccessToken> response = restClient.post()
//                .uri(BASE_URL + "/login")
//                .body(request.getBody())
//                .retrieve()
//                .toEntity(AccessToken.class);
//
//        accessToken = response.getBody().getAccessToken();
//    }

    @Test
    void createTest(){
        ArticleCreateRequest request = new ArticleCreateRequest("한글 검색", "my content", 1L, 1L);

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
    void searchTest(){
        ArticlePageResponse response = search("새벽", 0, 500);
        System.out.println("response = " + response.getArticleResponses().size());
        for(ArticleResponse articleResponse : response.getArticleResponses()){
            System.out.println("articleResponse = " + articleResponse.getTitle());
        }
    }
    @Test
    void readAllTestWithCorvering(){
        List<Long> responseTimes = new ArrayList<>();

        long start = System.currentTimeMillis();
        ArticlePageResponse response = readAllByBoardIdWithCorvering(1L,301L,0L);
        long duration = System.currentTimeMillis() - start;
        responseTimes.add(duration);

        List<ArticleResponse> firstPageArticles = response.getArticleResponses();
        if (firstPageArticles.isEmpty()) {
            System.out.println("조회된 Article이 없습니다.");
            return;
        }

        for (int i = 0; i<999; i++) {
            start = System.currentTimeMillis();
            response = readAllByBoardIdWithCorvering(1L,301L, (300* (long)i + 1));
            duration = System.currentTimeMillis() - start;
            responseTimes.add(duration);

            System.out.println("duration = "+ i + " 번째" + duration);
            List<ArticleResponse> articles = response.getArticleResponses();
            if (articles.isEmpty()) {
                break;
            }
        }

        Collections.sort(responseTimes);
        long p999 = responseTimes.get(responseTimes.size() - 1);
        double average = responseTimes.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0);

        // 결과 출력
        System.out.println("전체 실행 횟수: " + responseTimes.size());
        System.out.println("평균 응답 시간: " + average + " ms");
        System.out.println("p999 응답 시간: " + p999 + " ms");
    }
    @Test
    void readAllTest(){
        List<Long> responseTimes = new ArrayList<>();

        long start = System.currentTimeMillis();
        ArticlePageResponse response = readAllFirstPageByBoardId(1L, 301L);
        long duration = System.currentTimeMillis() - start;
        responseTimes.add(duration);

        List<ArticleResponse> firstPageArticles = response.getArticleResponses();
        if (firstPageArticles.isEmpty()) {
            System.out.println("조회된 Article이 없습니다.");
            return;
        }
        long lastArticleId = firstPageArticles.get(firstPageArticles.size() - 1).getArticleId();

        for (int i = 0; i <999; i++) {
            start = System.currentTimeMillis();
            response = readAllOtherPageByBoardId(1L, 301L, lastArticleId);
            duration = System.currentTimeMillis() - start;
            responseTimes.add(duration);

            List<ArticleResponse> articles = response.getArticleResponses();
            if (articles.isEmpty()) {
                break;
            }
            lastArticleId = articles.get(articles.size() - 1).getArticleId();
        }
        Collections.sort(responseTimes);


        long p999 = responseTimes.get(responseTimes.size() - 1);

        double average = responseTimes.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0);

        // 결과 출력
        System.out.println("전체 실행 횟수: " + responseTimes.size());
        System.out.println("평균 응답 시간: " + average + " ms");
        System.out.println("p999 응답 시간: " + p999 + " ms");
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

    ArticlePageResponse readAllByBoardIdWithCorvering(Long boardId, Long limit, Long offset){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path( "/board/{boardId}/articles/cv")
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build(boardId))
                .retrieve()
                .body(ArticlePageResponse.class);
    }

    ArticlePageResponse search(String title, Integer page, Integer size){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path( "/search")
                        .queryParam("title", title)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .body(ArticlePageResponse.class);
    }

    @Getter
    @AllArgsConstructor
    static class ArticleSearchRequest{
        private String title;
        private Integer page;
        private Integer size;
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

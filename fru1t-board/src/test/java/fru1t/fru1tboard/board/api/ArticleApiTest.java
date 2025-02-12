package fru1t.fru1tboard.board.api;

import fru1t.fru1tboard.board.response.ArticleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import lombok.AllArgsConstructor;
import lombok.Getter;


public class ArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:8080");

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
                    .uri("/api/v1/articles/{articleId}", 120115454734336L)
                    .retrieve()
                    .toBodilessEntity();
    }
    ArticleResponse update(Long articleId, ArticleUpdateRequest request){
        return restClient.put()
                .uri("/api/v1/articles/{articleId}",articleId)
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);

    }
    ArticleResponse create(ArticleCreateRequest request){
        return restClient.post()
                .uri("/api/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);

    }
    ArticleResponse read(Long articleId){
        return restClient.get()
                .uri("/api/v1/articles/{articleId}",articleId)
                .retrieve()
                .body(ArticleResponse.class);

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

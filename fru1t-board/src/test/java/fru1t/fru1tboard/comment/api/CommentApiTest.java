package fru1t.fru1tboard.comment.api;


import fru1t.fru1tboard.comment.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class CommentApiTest {
    RestClient restClient = RestClient.create("http://localhost:8080");

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
                .uri("/v1/comments/{commentsId}", 120115454734336L)
                .retrieve()
                .toBodilessEntity();
    }
    CommentResponse update(Long commentId, CommentUpdateRequest request){
        return restClient.put()
                .uri("/v1/comments/{commentId}",commentId)
                .body(request)
                .retrieve()
                .body(CommentResponse.class);

    }
    CommentResponse create(CommentCreateRequest request){
        return restClient.post()
                .uri("/v1/comments/")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);

    }

    CommentResponse read(Long commentId){
        return restClient.get()
                .uri("/v1/comments/{commentsId}",commentId)
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

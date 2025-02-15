package fru1t.fru1tboard.board;

import fru1t.fru1tboard.board.entity.Article;
import fru1t.fru1tboard.board.request.ArticleCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class ArticleRepositoryTest {
    @Autowired
    ArticleRepository articleRepository;

    @Test
    void saveTest(){
        ArticleCreateRequest request = new ArticleCreateRequest("my title", "my content", 1L, 1L);
        Article article = Article.create(1L,
                request.getTitle(),
                request.getContent(),
                request.getBoardId(),
                request.getWriteId()
        );
        Article response = articleRepository.save(article);
        System.out.println("response = " + response.getArticleId());
    }
    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest{
        private String title;
        private String content;
        private Long boardId;
        private Long writeId;
    }

    @Test
    void readAllTest() {
    }

    @Getter
    @AllArgsConstructor
    static class PagenationRequest{
    }
}
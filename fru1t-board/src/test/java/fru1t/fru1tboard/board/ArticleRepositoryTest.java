package fru1t.fru1tboard.board;

import fru1t.fru1tboard.board.entity.Article;
import fru1t.fru1tboard.board.entity.ArticleSearchEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ArticleRepositoryTest {
    @Autowired
    ArticleRepository articleCommandRepository;

    @Autowired
    ArticleSearchRepository articleQueryRepository;

    @Test
    void searchTest(){
        ArticleCreateRequest request = new ArticleCreateRequest("my title2", "my content", 1L, 1L);
        Article article = Article.create(2L,
                request.getTitle(),
                request.getContent(),
                request.getBoardId(),
                request.getWriteId()
        );
        Article response = articleCommandRepository.save(article);
        List<ArticleSearchEntity> responses = articleQueryRepository.searchByTitle("my title", 0, 10);
        System.out.println("responses = " + responses.size());
        for (ArticleSearchEntity articleSearchEntity : responses) {
            System.out.println("articleSearchEntity = " + articleSearchEntity.getTitle());
        }
        System.out.println("response = " + response.getArticleId());
    }
    @Test
    void saveTest(){
        ArticleCreateRequest request = new ArticleCreateRequest("my title", "my content", 1L, 1L);
        Article article = Article.create(1L,
                request.getTitle(),
                request.getContent(),
                request.getBoardId(),
                request.getWriteId()
        );
        Article response = articleCommandRepository.save(article);
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
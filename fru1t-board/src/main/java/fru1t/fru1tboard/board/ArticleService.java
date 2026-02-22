package fru1t.fru1tboard.board;

import fru1t.fru1tboard.board.entity.Article;
import fru1t.fru1tboard.board.request.ArticleCreateRequest;
import fru1t.fru1tboard.board.request.ArticleUpdateRequest;
import fru1t.fru1tboard.board.response.ArticlePageResponse;
import fru1t.fru1tboard.board.response.ArticleResponse;
import fru1t.fru1tboard.common.Snowflake;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final Snowflake snowflake = new Snowflake();
    private final ArticleRepository articleRepository;

    @Transactional
    public ArticleResponse create(ArticleCreateRequest articleCreateRequest) {
        Article article = articleRepository.save(Article.create(
                snowflake.nextId(),
                articleCreateRequest.getTitle(),
                articleCreateRequest.getContent(),
                articleCreateRequest.getBoardId(),
                articleCreateRequest.getWriterId()
        ));

        return ArticleResponse.from(article);
    }

    public ArticleResponse read(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        return ArticleResponse.from(article);
    }

    @Transactional
    public void delete(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        articleRepository.delete(article);
    }

    @Transactional
    public ArticleResponse update(Long articleId, ArticleUpdateRequest articleUpdateRequest){
        Article article = articleRepository.findById(articleId).orElseThrow();
        article.update(articleUpdateRequest.getTitle(), articleUpdateRequest.getContent());
        return ArticleResponse.from(article);
    }

    public ArticlePageResponse readAll(Long pageSize, Long lastArticleId) {
      List<Article> articles = (lastArticleId == null ?
                articleRepository.findAll(pageSize) :
                articleRepository.findAll(pageSize, lastArticleId));

        return ArticlePageResponse.create(articles);
    }

    public ArticlePageResponse readAllByBoardId(Long boardId, Long pageSize, Long lastArticleId) {
        List<Article> articles = (lastArticleId == null ?
                articleRepository.findAllByBoardId(boardId, pageSize) :
                articleRepository.findAllByBoardId(boardId, pageSize, lastArticleId));

        return ArticlePageResponse.create(articles);
    }

    public ArticlePageResponse readAllByBoardIdCorvering(Long boardId, Long limit, Long offset){
        List<Article> articles = articleRepository.findAllByBoardIdWithCorvering(boardId, limit, offset);
        return ArticlePageResponse.create(articles);
    }
}

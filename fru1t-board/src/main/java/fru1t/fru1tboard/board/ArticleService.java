package fru1t.fru1tboard.board;

import fru1t.fru1tboard.board.entity.Article;
import fru1t.fru1tboard.board.request.ArticleCreateRequest;
import fru1t.fru1tboard.board.request.ArticleUpdateRequest;
import fru1t.fru1tboard.board.response.ArticleResponse;
import fru1t.fru1tboard.comment.CommentRepository;
import fru1t.fru1tboard.comment.entity.Comment;
import fru1t.fru1tboard.common.Snowflake;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final Snowflake snowflake = new Snowflake();
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

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
}

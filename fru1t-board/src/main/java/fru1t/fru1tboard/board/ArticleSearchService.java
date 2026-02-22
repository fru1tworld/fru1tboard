package fru1t.fru1tboard.board;

import fru1t.fru1tboard.board.entity.Article;
import fru1t.fru1tboard.board.entity.ArticleSearchEntity;
import fru1t.fru1tboard.board.response.ArticlePageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleSearchService {
    private final ArticleSearchRepository articleSearchRepository;
    private final ArticleRepository articleRepository;

    public ArticlePageResponse search(String title, Integer page, Integer size){
        List<Article> articles = getArticles(articleSearchRepository.searchByTitle(title, page, size));
        return ArticlePageResponse.create(articles);
    }

    private List<Article> getArticles(List<ArticleSearchEntity> articleSearchEntities){
        List<Article> articles = new ArrayList<>();
        for (ArticleSearchEntity articleSearchEntity : articleSearchEntities) {
            Article article = articleRepository.findById(articleSearchEntity.getArticleId()).orElse(null);
            articles.add(article);
        }
        return articles;
    }
}

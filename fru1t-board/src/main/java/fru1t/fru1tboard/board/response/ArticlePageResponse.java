package fru1t.fru1tboard.board.response;

import fru1t.fru1tboard.board.entity.Article;
import lombok.Getter;

import java.util.List;

@Getter
public class ArticlePageResponse {
    private List<ArticleResponse> articleResponses;

    public static ArticlePageResponse create(List<Article> articles) {
        ArticlePageResponse response = new ArticlePageResponse();
        response.articleResponses = articles.stream().map(ArticleResponse::from).toList();
        return response;
    }
}

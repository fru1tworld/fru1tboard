package fru1t.fru1tboard.board;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import fru1t.fru1tboard.board.entity.ArticleSearchEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ArticleSearchRepository {
    private final ElasticsearchOperations elasticsearchOperations;

    public List<ArticleSearchEntity> searchByTitle(String title, Integer page, Integer size) {
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(QueryBuilders.multiMatch()
                        .fields(List.of("title^3", "title.ngram^2", "title.chosung"))
                        .query(title)
                        .build()
                        ._toQuery())
                .withPageable(PageRequest.of(page, size))
                .build();

        SearchHits<ArticleSearchEntity> searchHits = elasticsearchOperations.search(searchQuery, ArticleSearchEntity.class);

        return searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());
    }
}

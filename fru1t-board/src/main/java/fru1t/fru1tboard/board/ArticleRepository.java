package fru1t.fru1tboard.board;

import fru1t.fru1tboard.board.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    @Query(
            value = """
                SELECT * 
                FROM article 
                ORDER BY article_id DESC
                LIMIT :limit""",
            nativeQuery = true
    )
    List<Article> findAll(
            @Param("limit") Long limit
    );

    @Query(
            value = """
                SELECT * 
                FROM article 
                WHERE article_id < :lastArticleId 
                ORDER BY article_id DESC
                LIMIT :limit""",
            nativeQuery = true
    )
    List<Article> findAll(
            @Param("limit") Long limit,
            @Param("lastArticleId") Long lastArticleId
    );

    @Query(
            value = """
                SELECT * 
                FROM article  
                WHERE board_id = :boardId
                ORDER BY article_id DESC
                LIMIT :limit""",
            nativeQuery = true
    )
    List<Article> findAllByBoardId(
            @Param("boardId") Long boardId,
            @Param("limit") Long limit
    );

    @Query(
            value = """
            WITH board_articles AS (
                SELECT article_id
                FROM article
                WHERE board_id = :boardId
                ORDER BY article_id DESC
                LIMIT :limit OFFSET :offset
                )
            SELECT a.*
            FROM article a
            JOIN board_articles b ON a.article_id = b.article_id
            ORDER BY a.article_id DESC;""",
            nativeQuery = true
    )
    List<Article> findAllByBoardIdWithCorvering(
            @Param("boardId") Long boardId,
            @Param("limit") Long limit,
            @Param("offset") Long offset
    );

    @Query(
            value = """ 
                SELECT * 
                FROM article
                WHERE board_id = :boardId AND article_id < :lastArticleId 
                ORDER BY article_id DESC
                LIMIT :limit""",
            nativeQuery = true
    )
    List<Article> findAllByBoardId(
            @Param("boardId") Long boardId,
            @Param("limit") Long limit,
            @Param("lastArticleId") Long lastArticleId
    );

    @Query(
            value = """
                        SELECT *
                        FROM article
                        WHERE MATCH(title) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
                        ORDER BY article_id DESC
                        LIMIT :limit OFFSET :offset""",
            nativeQuery = true
    )
    List<Article> searchArticles(
            @Param("keyword") String keyword,
            @Param("limit") Long limit,
            @Param("offset") Long offset
    );
}

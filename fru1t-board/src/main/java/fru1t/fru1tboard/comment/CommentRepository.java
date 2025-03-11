package fru1t.fru1tboard.comment;

import fru1t.fru1tboard.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value =
            """
            SELECT c.comment_id, c.article_id, c.content, c.parent_comment_id, c.writer_id, c.deleted, c.created_at, c.modified_at 
                    FROM comment c 
                    JOIN ( 
                       SELECT comment_id AS t_comment_id 
                       FROM comment 
                       ORDER BY comment_id DESC 
                       LIMIT :limit) t 
                    ON c.comment_id = t.t_comment_i""",
            nativeQuery = true)
    List<Comment> findAll(
            @Param("articleId") Long articleId,
            @Param("limit") Long limit
    );

    @Query(value =
            """
            SELECT c.comment_id, c.article_id, c.content, c.parent_comment_id, c.writer_id, c.deleted, c.created_at, c.modified_at 
                    FROM comment c 
                    JOIN ( 
                       SELECT comment_id AS t_comment_id 
                       FROM comment 
                       WHERE article_id = :articleId AND comment_id < :lastCommentId 
                       ORDER BY comment_id DESC 
                       LIMIT :limit) t 
                    ON c.comment_id = t.t_comment_i""",
            nativeQuery = true)
    List<Comment> findAll(
            @Param("articleId") Long articleId,
            @Param("limit") Long limit,
            @Param("lastCommentId") Long lastCommentId
    );

    @Query(
            value =
                    """
                    select count(*) from (
                       select comment_id from comment 
                       where article_id = :articleId and parent_comment_id = :parentCommentId 
                       limit :limit
                    )""",
            nativeQuery = true
    )
    Long countBy(
            @Param("articleId") Long articleId,
            @Param("parentCommentId") Long parentCommentId,
            @Param("limit") Long limit
    );
}

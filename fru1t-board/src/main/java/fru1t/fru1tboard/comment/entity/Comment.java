package fru1t.fru1tboard.comment.entity;

import fru1t.fru1tboard.comment.request.CommentUpdateRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    private Long commentId;
    private Long articleId;
    private String content;
    private Long parentCommentId;
    private Long writerId;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Comment create(Long commentId, Long articleId, String content, Long parentCommentId, Long writerId) {
        Comment comment = new Comment();
        comment.commentId = commentId;
        comment.articleId = articleId;
        comment.content = content;
        comment.parentCommentId = parentCommentId == null ? commentId : parentCommentId;
        comment.writerId = writerId;
        comment.deleted = false;
        comment.createdAt = LocalDateTime.now();
        comment.modifiedAt = comment.createdAt;
        return comment;
    }

    public void  update(CommentUpdateRequest commentUpdateRequest){
        this.content = commentUpdateRequest.getContent();
        modifiedAt = LocalDateTime.now();
    }

    public Boolean isDeleted() {
        return this.deleted;
    }

    public Boolean isRoot(){
        return this.commentId == this.parentCommentId;
    }

    public void delete(){
        this.deleted = true;
    }
}

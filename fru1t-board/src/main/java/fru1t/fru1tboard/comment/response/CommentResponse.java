package fru1t.fru1tboard.comment.response;

import fru1t.fru1tboard.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponse {
    private Long commentId;
    private Long parentCommentId;
    private String Content;
    private Long writerId;
    private Boolean isDeleted;
    private Boolean isModified;

    public static CommentResponse from(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.commentId = comment.getCommentId();
        response.parentCommentId = comment.getParentCommentId();
        response.Content = comment.getContent();
        response.writerId = comment.getWriterId();
        response.isDeleted = comment.getDeleted();
        response.isModified = comment.getCreatedAt() != comment.getModifiedAt();
        return response;

    }
}

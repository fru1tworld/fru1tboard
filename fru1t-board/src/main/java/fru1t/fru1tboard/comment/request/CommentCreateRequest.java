package fru1t.fru1tboard.comment.request;


import lombok.Getter;

@Getter
public class CommentCreateRequest {
    private String content;
    private Long articleId;
    private Long parentCommentId;
    private Long writerId;
}

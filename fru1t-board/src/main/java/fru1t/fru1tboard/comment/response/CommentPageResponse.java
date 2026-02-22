package fru1t.fru1tboard.comment.response;

import fru1t.fru1tboard.comment.entity.Comment;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentPageResponse {
    List<CommentResponse> commentResponse;

    public static CommentPageResponse create(List<Comment> comments) {
        CommentPageResponse response = new CommentPageResponse();
        response.commentResponse = comments.stream().map(CommentResponse::from).toList();
        return response;
    }
}

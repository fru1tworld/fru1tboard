package fru1t.fru1tboard.comment;

import fru1t.fru1tboard.comment.request.CommentCreateRequest;
import fru1t.fru1tboard.comment.request.CommentUpdateRequest;
import fru1t.fru1tboard.comment.response.CommentPageResponse;
import fru1t.fru1tboard.comment.response.CommentResponse;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/v1/comments/")
    public CommentResponse create(@RequestBody CommentCreateRequest request) {
        CommentResponse response = commentService.create(request);
        return response;
    }

    @GetMapping("/v1/comments/{commentId}")
    public CommentResponse read(@PathVariable Long commentId) {
        CommentResponse response = commentService.read(commentId);
        return response;
    }

    @PutMapping("/v1/comments/{commentId}")
    public CommentResponse update(@PathVariable Long commentId,
                                  @RequestBody CommentUpdateRequest request) {
        CommentResponse response = commentService.update(commentId, request);
        return response;
    }
    @DeleteMapping("/v1/comments/{commentId}")
    public void delete(@PathVariable Long commentId) {
         commentService.delete(commentId);
    }

    @GetMapping("/v1/articles/{articleId}/comments")
    public CommentPageResponse readAll(@PathVariable Long articleId,
                                       @RequestParam(defaultValue = "300") Long pageSize,
                                       @RequestParam(required = false) Long lastArticleId){
        return commentService.readAll(articleId, pageSize, lastArticleId);
    }
}

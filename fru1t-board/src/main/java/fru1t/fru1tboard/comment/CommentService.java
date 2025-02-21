package fru1t.fru1tboard.comment;


import fru1t.fru1tboard.comment.entity.Comment;
import fru1t.fru1tboard.comment.request.CommentCreateRequest;
import fru1t.fru1tboard.comment.request.CommentUpdateRequest;
import fru1t.fru1tboard.comment.response.CommentPageResponse;
import fru1t.fru1tboard.comment.response.CommentResponse;
import fru1t.fru1tboard.common.Snowflake;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

import static java.util.function.Predicate.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final Snowflake snowflake = new Snowflake();
    private final CommentRepository commentRepository;

    @Transactional
    CommentResponse create(CommentCreateRequest request){
        Comment parent = findParent(request.getParentCommentId());
        Comment comment = commentRepository.save(Comment.create(
                snowflake.nextId(),
                request.getArticleId(),
                request.getContent(),
                parent == null ? null : parent.getParentCommentId(),
                request.getWriterId()));
        return CommentResponse.from(comment);
    }

    CommentResponse read(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        return CommentResponse.from(comment);
    }

    Comment findParent(Long parentId){
        if(parentId == null){
            return null;
        }
        return commentRepository.findById(parentId).orElseThrow();
    }

    @Transactional
    CommentResponse update(Long commentId, CommentUpdateRequest request){
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.update(request);
        return CommentResponse.from(comment);
    }

    @Transactional
    void delete(Long commentId){
        commentRepository.findById(commentId)
                .filter(not(Comment::getDeleted))
                .ifPresent(comment -> {
                    if (hasChildren(comment)) {
                        comment.delete();
                    } else {
                        delete(comment);
                    }
                });
    }

    private void delete(Comment comment){
        commentRepository.delete(comment);
        if(!comment.isRoot()){
            commentRepository.findById(comment.getParentCommentId())
                    .filter(Comment::getDeleted)
                    .filter(not(this::hasChildren))
                    .ifPresent(this::delete);
        }
    }

    private boolean hasChildren(Comment comment){
        return commentRepository.countBy(comment.getArticleId(), comment.getCommentId(), 2L) == 2;
    }

    CommentPageResponse readAll(Long articleId, Long pageSize, Long lastCommentId){
         List<Comment> comments = lastCommentId == null ?
                commentRepository.findAll(articleId, pageSize) :
                commentRepository.findAll(articleId, pageSize, lastCommentId);

         return CommentPageResponse.create(comments);

    }


}

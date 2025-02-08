package fru1t.fru1tboard.comment;


import fru1t.fru1tboard.board.ArticleRepository;
import fru1t.fru1tboard.comment.entity.Comment;
import fru1t.fru1tboard.comment.request.CommentCreateRequest;
import fru1t.fru1tboard.comment.request.CommentUpdateRequest;
import fru1t.fru1tboard.comment.response.CommentResponse;
import fru1t.fru1tboard.common.Snowflake;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        commentRepository.delete(comment);
    }
}

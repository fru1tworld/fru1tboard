package fru1t.fru1tboard.comment;

import fru1t.fru1tboard.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@SpringBootTest
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;
    @Test
    void readAll(){
        PagenationCommentRequest request = new PagenationCommentRequest(1L, 30L, 9350000L);
        Instant startTime = Instant.now();
        List<Comment> comments = commentRepository.findAll(request.getArticleId(), request.getLimit(), request.getOffset());
        Instant endTime = Instant.now();
        long elapsedTimeMillis = Duration.between(startTime, endTime).toMillis();
        System.out.println("Execution time = " + elapsedTimeMillis + " ms");
        System.out.println("comments = " + comments.size());
        for(Comment comment : comments){
            System.out.println("comment = " + comment.getCommentId());
        }
    }
    @Getter
    @AllArgsConstructor
    static class PagenationCommentRequest{
        private Long articleId;
        private Long limit;
        private Long offset;
    }
}
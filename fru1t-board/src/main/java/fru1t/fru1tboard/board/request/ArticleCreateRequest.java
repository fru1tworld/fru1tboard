package fru1t.fru1tboard.board.request;

import lombok.Getter;

@Getter
public class ArticleCreateRequest {
    private String title;
    private String content;
    private Long boardId;
    private Long writerId;
}

package fru1t.fru1tboard.board;

import fru1t.fru1tboard.board.request.ArticleCreateRequest;
import fru1t.fru1tboard.board.request.ArticleUpdateRequest;
import fru1t.fru1tboard.board.response.ArticlePageResponse;
import fru1t.fru1tboard.board.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleSearchService articleSearchService;

    @PostMapping("/v1/articles")
    public ArticleResponse create(@RequestBody ArticleCreateRequest request){
        ArticleResponse response = articleService.create(request);
        return response;
    }

    @GetMapping("/v1/articles/{articleId}")
    public ArticleResponse read(@PathVariable Long articleId){
        ArticleResponse response = articleService.read(articleId);
        return response;
    }
    @PutMapping("/v1/articles/{articleId}")
    public ArticleResponse update(@PathVariable Long articleId, @RequestBody ArticleUpdateRequest request){
        ArticleResponse response = articleService.update(articleId, request);
        return response;
    }

    @DeleteMapping("/v1/articles/{articleId}")
    public void delete(@PathVariable Long articleId){
        System.out.println("Delete request received for articleId: " + articleId);
        articleService.delete(articleId);
    }

    @GetMapping("v1/board/all/articles")
    public ArticlePageResponse readAllByboardId(@RequestParam("pageSize") Long pageSize,
                                                @RequestParam(value = "lastArticleId" , required = false) Long lastArticleId
    ) {
        return articleService.readAll(pageSize, lastArticleId);
    }
    @GetMapping("v1/board/{boardId}/articles")
    public ArticlePageResponse readAllByboardId(@PathVariable Long boardId,
                                       @RequestParam("pageSize") Long pageSize,
                                       @RequestParam(value = "lastArticleId", required = false) Long lastArticleId
                                       ) {
        return articleService.readAllByBoardId(boardId, pageSize, lastArticleId);
    }

    @GetMapping("v1/board/{boardId}/articles/cv")
    public ArticlePageResponse readAllByboardIdCorvering(@PathVariable Long boardId,
                                                @RequestParam("limit") Long limit,
                                                @RequestParam("offset") Long offset
    ) {
        return articleService.readAllByBoardIdCorvering(boardId, limit, offset);
    }

    @GetMapping("v1/search")
    public ArticlePageResponse readAllSearch(@RequestParam("title") String title,
                                             @RequestParam("page") Integer page,
                                             @RequestParam("size") Integer size){
        return articleSearchService.search(title, page, size);
    }

}

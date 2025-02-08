package fru1t.fru1tboard.board;

import fru1t.fru1tboard.board.request.ArticleCreateRequest;
import fru1t.fru1tboard.board.request.ArticleUpdateRequest;
import fru1t.fru1tboard.board.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

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
        System.out.println("Delete request received for articleId: " + articleId);  // 로그 추가
        articleService.delete(articleId);
    }

}

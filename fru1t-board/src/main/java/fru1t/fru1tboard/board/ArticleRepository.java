package fru1t.fru1tboard.board;

import fru1t.fru1tboard.board.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
}

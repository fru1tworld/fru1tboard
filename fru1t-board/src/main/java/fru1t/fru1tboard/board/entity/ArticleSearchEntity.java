package fru1t.fru1tboard.board.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
@Getter
@Document(indexName = "mysql.fru1t_board.article_nori.v2")
public class ArticleSearchEntity {
    @Id
    @Field(name = "article_id", type = FieldType.Long)
    private Long articleId;

    @Field(name = "title", type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private LocalDateTime createdAt;
}
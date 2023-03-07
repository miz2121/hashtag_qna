package portfolio.project.hashtagqna.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnCommentDto {
    private Long answerId;
    private String writer;
    private LocalDateTime date;
    private String content;

    public AnCommentDto() {
    }

    @QueryProjection
    public AnCommentDto(Long answerId, String writer, LocalDateTime date, String content) {
        this.answerId = answerId;
        this.writer = writer;
        this.date = date;
        this.content = content;
    }
}

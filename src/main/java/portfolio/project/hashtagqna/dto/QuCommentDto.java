package portfolio.project.hashtagqna.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuCommentDto {
    private String writer;
    private LocalDateTime date;
    private String content;

    public QuCommentDto() {
    }

    @QueryProjection
    public QuCommentDto(String writer, LocalDateTime date, String content) {
        this.writer = writer;
        this.date = date;
        this.content = content;
    }
}

package portfolio.project.hashtagqna.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnCommentDto {
    private Long id;
    private Long answerId;
    private String writer;
    private LocalDateTime date;
    private String content;

    private boolean editable;

    public AnCommentDto() {
    }

    @QueryProjection
    public AnCommentDto(Long id, Long answerId, String writer, LocalDateTime date, String content, boolean editable) {
        this.id = id;
        this.answerId = answerId;
        this.writer = writer;
        this.date = date;
        this.content = content;
        this.editable = editable;
    }
}

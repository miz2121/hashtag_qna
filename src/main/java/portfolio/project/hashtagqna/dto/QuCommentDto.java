package portfolio.project.hashtagqna.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuCommentDto {
    private Long id;
    private String writer;
    private LocalDateTime date;
    private String content;
    private boolean editable;

    public QuCommentDto() {
    }

    @QueryProjection
    public QuCommentDto(Long id,String writer, LocalDateTime date, String content, boolean editable) {
        this.id = id;
        this.writer = writer;
        this.date = date;
        this.content = content;
        this.editable = editable;
    }
}

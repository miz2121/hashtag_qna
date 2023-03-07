package portfolio.project.hashtagqna.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import portfolio.project.hashtagqna.entity.AnswerStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AnswerDto {
    private Long id;
    private String writer;
    private LocalDateTime date;
    private String content;
    private AnswerStatus answerStatus;
    private int anCommentCount;
    private int rating;

    public AnswerDto() {
    }

    @QueryProjection
    public AnswerDto(Long id, String writer, LocalDateTime date, String content, AnswerStatus answerStatus, int anCommentCount, int rating) {
        this.id = id;
        this.writer = writer;
        this.date = date;
        this.content = content;
        this.answerStatus = answerStatus;
        this.anCommentCount = anCommentCount;
        this.rating = rating;
    }
}

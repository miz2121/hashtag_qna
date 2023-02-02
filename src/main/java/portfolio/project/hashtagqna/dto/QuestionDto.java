package portfolio.project.hashtagqna.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionDto {
    private String writer;
    private String closed;
    private int answerCount;
    private LocalDateTime date;

    public QuestionDto() {
    }

    @QueryProjection
    public QuestionDto(String writer, String closed, int answerCount, LocalDateTime date) {
        this.writer = writer;
        this.closed = closed;
        this.answerCount = answerCount;
        this.date = date;
    }
}

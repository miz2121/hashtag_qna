package portfolio.project.hashtagqna.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import portfolio.project.hashtagqna.entity.QuestionStatus;

import java.time.LocalDateTime;

@Data
public class QuestionListDto {
    private String writer;
    private QuestionStatus questionStatus;
    private int answerCount;
    private LocalDateTime date;

    public QuestionListDto() {
    }

    @QueryProjection
    public QuestionListDto(String writer, QuestionStatus questionStatus, int answerCount, LocalDateTime date) {
        this.writer = writer;
        this.questionStatus = questionStatus;
        this.answerCount = answerCount;
        this.date = date;
    }
}

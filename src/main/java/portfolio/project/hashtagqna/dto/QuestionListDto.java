package portfolio.project.hashtagqna.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import portfolio.project.hashtagqna.entity.QuestionStatus;

import java.time.LocalDateTime;

@Data
public class QuestionListDto {
    private Long id;
    private String writer;
    private String title;
    private QuestionStatus questionStatus;
    private int answerCount;
    private LocalDateTime date;
    private HashtagListDto hashtagListDto;

    public QuestionListDto() {
    }

    @QueryProjection
    public QuestionListDto(Long id, String writer, String title, QuestionStatus questionStatus, int answerCount, LocalDateTime date) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.questionStatus = questionStatus;
        this.answerCount = answerCount;
        this.date = date;
    }

    @Builder
    public QuestionListDto(Long id, String writer, String title, QuestionStatus questionStatus, int answerCount, LocalDateTime date, HashtagListDto hashtagListDto) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.questionStatus = questionStatus;
        this.answerCount = answerCount;
        this.date = date;
        this.hashtagListDto = hashtagListDto;
    }
}

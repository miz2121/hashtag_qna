package portfolio.project.hashtagqna.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import portfolio.project.hashtagqna.entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionDto {
    private Long id;
    private String title;
    private String writer;
    private LocalDateTime date;
    private String content;
    private QuestionStatus questionStatus;
    private int quCommentCount;
    private int answerCount;
    private boolean editable;

    public QuestionDto() {
    }

    @QueryProjection
    public QuestionDto(Long id, String title, String writer, LocalDateTime date, String content, QuestionStatus questionStatus, int quCommentCount, int answerCount, boolean editable) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.date = date;
        this.content = content;
        this.questionStatus = questionStatus;
        this.quCommentCount = quCommentCount;
        this.answerCount = answerCount;
        this.editable = editable;
    }
}

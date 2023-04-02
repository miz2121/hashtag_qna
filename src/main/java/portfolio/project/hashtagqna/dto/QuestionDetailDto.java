package portfolio.project.hashtagqna.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 질문글 하나 클릭했을 때 필요한 DTO
 */
@Data
public class QuestionDetailDto {
    private QuestionDto questionDto;
    private List<HashtagDto> hashtagDtos = new ArrayList<>();
    private List<AnswerDto> answerDtos = new ArrayList<>();
    private List<QuCommentDto> quCommentDtos = new ArrayList<>();
    private List<AnCommentDto> anCommentDtos = new ArrayList<>();

    public QuestionDetailDto() {
    }

    @Builder
    public QuestionDetailDto(QuestionDto questionDto, List<HashtagDto> hashtagDtos, List<AnswerDto> answerDtos, List<QuCommentDto> quCommentDtos, List<AnCommentDto> anCommentDtos) {
        this.questionDto = questionDto;
        this.hashtagDtos = hashtagDtos;
        this.answerDtos = answerDtos;
        this.quCommentDtos = quCommentDtos;
        this.anCommentDtos = anCommentDtos;
    }
}

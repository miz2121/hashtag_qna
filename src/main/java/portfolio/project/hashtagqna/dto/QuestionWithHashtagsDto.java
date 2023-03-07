package portfolio.project.hashtagqna.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionWithHashtagsDto {
    private QuestionDto questionDto;
    private List<HashtagDto> hashtagDtos;
    private List<AnswerDto> answerDtos;

    public QuestionWithHashtagsDto() {
    }

    public QuestionWithHashtagsDto(QuestionDto questionDto, List<HashtagDto> hashtagDtos, List<AnswerDto> answerDtos) {
        this.questionDto = questionDto;
        this.hashtagDtos = hashtagDtos;
        this.answerDtos = answerDtos;
    }
}

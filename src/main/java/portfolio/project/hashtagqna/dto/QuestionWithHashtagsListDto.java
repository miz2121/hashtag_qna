package portfolio.project.hashtagqna.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class QuestionWithHashtagsListDto {
    private Page<QuestionListDto> questionListDtoPage;
    private List<HashtagListDto> hashtagListDtoList;

    public QuestionWithHashtagsListDto() {
    }

    public QuestionWithHashtagsListDto(Page<QuestionListDto> questionListDtoPage, List<HashtagListDto> hashtagListDtoList) {
        this.questionListDtoPage = questionListDtoPage;
        this.hashtagListDtoList = hashtagListDtoList;
    }
}

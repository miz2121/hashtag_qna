package portfolio.project.hashtagqna.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class HomeQuestionWithHashtagsListDto {
   private List<QuestionListDto> questionListDtoList;
   private List<HashtagListDto> hashtagListDtoList;

    public HomeQuestionWithHashtagsListDto() {
    }

    public HomeQuestionWithHashtagsListDto(List<QuestionListDto> questionListDtoList, List<HashtagListDto> hashtagListDtoList) {
        this.questionListDtoList = questionListDtoList;
        this.hashtagListDtoList = hashtagListDtoList;
    }
}

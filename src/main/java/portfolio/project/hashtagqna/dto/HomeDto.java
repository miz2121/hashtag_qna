package portfolio.project.hashtagqna.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HomeDto {
    List<QuestionListDto> questionListDtos = new ArrayList<>();
    List<HashtagDto> hashtagDtos = new ArrayList<>();

    public HomeDto() {
    }

    public HomeDto(List<QuestionListDto> questionListDtos, List<HashtagDto> hashtagDtos) {
        this.questionListDtos = questionListDtos;
        this.hashtagDtos = hashtagDtos;
    }
}

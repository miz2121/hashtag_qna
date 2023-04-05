package portfolio.project.hashtagqna.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HomeDto {
    private HomeQuestionWithHashtagsListDto homeQuestionWithHashtagsListDto;
    private HomeHashtagListDto homeHashtagListDto;

    public HomeDto() {
    }

    public HomeDto(HomeQuestionWithHashtagsListDto homeQuestionWithHashtagsListDto, HomeHashtagListDto homeHashtagListDto) {
        this.homeQuestionWithHashtagsListDto = homeQuestionWithHashtagsListDto;
        this.homeHashtagListDto = homeHashtagListDto;
    }
}

package portfolio.project.hashtagqna.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateQuestionDto {
    private String title;
    private String content;
    private List<HashtagDto> existHashtagDtos;
    private List<HashtagDto> newHashtagDtos;

    public CreateQuestionDto() {
    }

    public CreateQuestionDto(String title, String content, List<HashtagDto> existHashtagDtos, List<HashtagDto> newHashtagDtos) {
        this.title = title;
        this.content = content;
        this.existHashtagDtos = existHashtagDtos;
        this.newHashtagDtos = newHashtagDtos;
    }
}

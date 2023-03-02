package portfolio.project.hashtagqna.dto;

import lombok.Data;

@Data
public class CreateQuestionDto {
    private String title;
    private String content;

    public CreateQuestionDto() {
    }

    public CreateQuestionDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

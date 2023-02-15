package portfolio.project.hashtagqna.dto;

import lombok.Data;

@Data
public class CreateAnswerDto {
    private String content;

    public CreateAnswerDto() {
    }

    public CreateAnswerDto(String content) {
        this.content = content;
    }
}

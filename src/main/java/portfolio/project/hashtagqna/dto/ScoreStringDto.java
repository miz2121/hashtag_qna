package portfolio.project.hashtagqna.dto;

import lombok.Data;

@Data
public class ScoreStringDto {
    private String scoreString;

    public ScoreStringDto() {
    }

    public ScoreStringDto(String scoreString) {
        this.scoreString = scoreString;
    }
}

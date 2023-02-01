package portfolio.project.hashtagqna.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MemberInfoDto {
    private String nickname;
    private String email;
    private int questionCount;
    private int answerCount;
    private int commentCount;
    private int hashtagCount;

    public MemberInfoDto() {
    }

    @QueryProjection
    public MemberInfoDto(String nickname, String email, int questionCount, int answerCount, int commentCount, int hashtagCount) {
        this.nickname = nickname;
        this.email = email;
        this.questionCount = questionCount;
        this.answerCount = answerCount;
        this.commentCount = commentCount;
        this.hashtagCount = hashtagCount;
    }
}

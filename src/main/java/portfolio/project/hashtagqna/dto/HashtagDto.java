package portfolio.project.hashtagqna.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import portfolio.project.hashtagqna.entity.Member;

@Data
public class HashtagDto {
    private String hashtagName;
    private Member member;

    public HashtagDto() {
    }

    @QueryProjection
    public HashtagDto(String hashtagName, Member member) {
        this.hashtagName = hashtagName;
        this.member = member;
    }
}

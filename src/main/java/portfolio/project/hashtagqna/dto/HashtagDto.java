package portfolio.project.hashtagqna.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import portfolio.project.hashtagqna.entity.Member;

@Data
public class HashtagDto {
    private String hashtagName;
    private String writer;

    public HashtagDto() {
    }

    @QueryProjection
    public HashtagDto(String hashtagName, String writer) {
        this.hashtagName = hashtagName;
        this.writer = writer;
    }
}

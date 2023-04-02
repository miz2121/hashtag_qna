package portfolio.project.hashtagqna.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import portfolio.project.hashtagqna.entity.MemberStatus;

@Data
public class MemberStatusDto {
    private Long id;
    private MemberStatus memberStatus;

    public MemberStatusDto() {
    }

    @QueryProjection
    public MemberStatusDto(Long id, MemberStatus memberStatus) {
        this.id = id;
        this.memberStatus = memberStatus;
    }
}

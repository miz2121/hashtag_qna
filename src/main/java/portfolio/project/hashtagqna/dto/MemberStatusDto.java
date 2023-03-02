package portfolio.project.hashtagqna.dto;

import lombok.Data;
import portfolio.project.hashtagqna.entity.MemberStatus;

@Data
public class MemberStatusDto {
    MemberStatus memberStatus;

    public MemberStatusDto() {
    }

    public MemberStatusDto(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }
}

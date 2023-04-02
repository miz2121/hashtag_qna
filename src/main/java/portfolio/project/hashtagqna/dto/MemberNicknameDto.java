package portfolio.project.hashtagqna.dto;

import lombok.Data;

@Data
public class MemberNicknameDto {
    String nickname;

    public MemberNicknameDto() {
    }

    public MemberNicknameDto(String nickname) {
        this.nickname = nickname;
    }
}

package portfolio.project.hashtagqna.dto;

import lombok.Data;

@Data
public class MemberNicknameDto {
    private String nickname;

    public MemberNicknameDto() {
    }

    public MemberNicknameDto(String nickname) {
        this.nickname = nickname;
    }
}

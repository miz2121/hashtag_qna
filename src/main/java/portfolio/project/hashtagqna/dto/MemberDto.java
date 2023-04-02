package portfolio.project.hashtagqna.dto;

import lombok.Data;

@Data
public class MemberDto {
    private String email;
    private String pwd;
    private String nickname;

    public MemberDto() {
    }

    public MemberDto(String email, String pwd, String nickname) {
        this.email = email;
        this.pwd = pwd;
        this.nickname = nickname;
    }
}

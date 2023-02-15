package portfolio.project.hashtagqna.dto;

import lombok.Data;

@Data
public class MemberDto {
    public String email;
    public String pwd;
    public String nickname;

    public MemberDto() {
    }

    public MemberDto(String email, String pwd, String nickname) {
        this.email = email;
        this.pwd = pwd;
        this.nickname = nickname;
    }
}

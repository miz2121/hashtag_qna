package portfolio.project.hashtagqna.dto;

import lombok.Data;

@Data
public class MemberLoginDto {
    private String email;
    private String pwd;

    public MemberLoginDto() {
    }

    public MemberLoginDto(String email, String pwd) {
        this.email = email;
        this.pwd = pwd;
    }
}

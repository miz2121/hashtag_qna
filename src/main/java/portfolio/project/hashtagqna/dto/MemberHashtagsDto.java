package portfolio.project.hashtagqna.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberHashtagsDto {
    private List<HashtagDto> hashtagDtos = new ArrayList<>();

    public MemberHashtagsDto() {
    }

    public MemberHashtagsDto(List<HashtagDto> hashtagDtos) {
        this.hashtagDtos = hashtagDtos;
    }
}

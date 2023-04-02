package portfolio.project.hashtagqna.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HashtagListDto {
    private List<HashtagDto> hashtagDtos = new ArrayList<>();

    public HashtagListDto() {
    }

    public HashtagListDto(List<HashtagDto> hashtagDtos) {
        this.hashtagDtos = hashtagDtos;
    }
}
